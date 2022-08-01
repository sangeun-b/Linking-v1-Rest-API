package com.example.linkingrest.config.security;

import com.example.linkingrest.config.security.dto.TokenDto;
import com.example.linkingrest.user.domain.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

@Slf4j
@RequiredArgsConstructor
@Component
@PropertySource(value = "classpath:application-jwt.yml")
public class JwtProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final Long accessTokenValidMillisecond = 60 * 60 * 1000L; // 1시간 토큰 유효

    private final Long refreshTokenValidMillisecond = 14 * 24 * 60 * 1000L; // 14 days

    private final CustomUserDetailsService userDetailsService;

    private String ROLE = "Role";

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // jwt 토큰 생성
    public TokenDto createTokenDto(Long userPk, Role role){
        Claims claims = Jwts.claims().setSubject(String.valueOf(userPk));
        claims.put(ROLE, role);

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + refreshTokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        return TokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(accessTokenValidMillisecond)
                .build();

    }

    // jwt 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) throws AuthenticationException {
        Claims claims = parseClaims(token);
        if(claims.get(ROLE) == null) {
            throw new AuthenticationException();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        log.info("userDetails :", String.valueOf(userDetails));
        log.info(userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    // jwt 토큰 복호화해서 가져오기
    private Claims parseClaims(String token){
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // HTTP Request의 Header에서 Token parsing -> "X-AUTH-TOKEN: jwt"
//    public String resolveToken(HttpServletRequest request){
//        return request.getHeader("X-AUTH-TOKEN");
//    }

    // http request header에서 token 추출
    public String extract(HttpServletRequest request, String type){
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()){
            String value = headers.nextElement();
            if(value.toLowerCase().startsWith(type.toLowerCase())){
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
    // JWT 의 유효성 및 만료일자 확인
    public boolean validationToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
//            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return !claimsJws.getBody().getExpiration().before(new Date()); // 만료 날짜가 현재보다 이전이면 false
        } catch (Exception e){
            return false;
        }
    }

}
