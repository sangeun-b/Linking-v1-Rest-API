package com.example.linkingrest.config.security.service;

import com.example.linkingrest.config.security.JwtProvider;
import com.example.linkingrest.config.security.RefreshTokenRepository;
import com.example.linkingrest.error.exception.EmailLoginFailedException;
import com.example.linkingrest.config.security.domain.RefreshToken;
import com.example.linkingrest.config.security.dto.TokenDto;
import com.example.linkingrest.config.security.dto.TokenRequest;
import com.example.linkingrest.user.controller.LoginUserRequest;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto login(LoginUserRequest request) {
        // 회원 존재 확인
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(EmailLoginFailedException::new);
        // 비밀번호 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new EmailLoginFailedException();

        // Access token, refresh token 발급
        TokenDto tokenDto = jwtProvider.createTokenDto(user.getId(),user.getRole());

        // Refresh token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;

    }
    @Transactional
    public TokenDto reissue(TokenRequest request) throws Exception {
        // 만료된 refresh token 에러
        if(!jwtProvider.validationToken(request.getRefreshToken())){
//            throw new RefreshTokenException();
            throw new Exception("REFRESH TOKEN IS EXPIRED!");
        }
        // access token 에서 username(pk)가져오기
        String accessToken = request.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        log.info(authentication.getName());
        log.info(String.valueOf(authentication.getAuthorities()));
        log.info(String.valueOf(authentication));

        // user pk로 user 검색/ 저장된 refresh token 없음
        log.info(authentication.getName());
        User user = userRepository.findById(Long.parseLong(authentication.getName())).orElseThrow(()->new NullPointerException("존재하지않는 회원입니다."));
        RefreshToken refreshToken = refreshTokenRepository.findByKey(user.getId())
                .orElseThrow(()-> new NullPointerException("토큰이 만료되었습니다."));

        // refresh token 불일치
        log.info(refreshToken.getToken());
        log.info(request.getRefreshToken());
        if(!refreshToken.getToken().equals(request.getRefreshToken()))
            throw new Exception("Refresh token 불일치");

        // access token, refresh token 재발급, refresh token 저장
        TokenDto newToken = jwtProvider.createTokenDto(user.getId(),user.getRole());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);

        return newToken;
    }


}
