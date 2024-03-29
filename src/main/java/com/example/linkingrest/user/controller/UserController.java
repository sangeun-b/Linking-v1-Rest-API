package com.example.linkingrest.user.controller;

import com.example.linkingrest.config.security.JwtProvider;
import com.example.linkingrest.config.security.dto.TokenDto;
import com.example.linkingrest.config.security.dto.TokenRequest;
import com.example.linkingrest.config.security.service.SecurityService;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = {"USER API V1"})
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;

    @ApiOperation(value = "회원 가입", notes = "새 회원 추가")
    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> saveUser(@RequestBody @ApiParam(value = "회원 가입에 필요한 정보", required = true) @Valid CreateUserRequest request){
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .img(request.getImg())
                .role(request.getRole())
                .build();
        Long id = userService.join(createUserRequest.toEntity());
//        return ResponseEntity.ok(new CreateUserResponse(id));
        return ResponseEntity.created(URI.create("/users/"+id)).body(new CreateUserResponse(id));
    }

    @ApiOperation(value = "로그인", notes = "Spring Security 를 이용한 로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @ApiParam(value = "로그인에 필요한 이메일과 비밀번호") @Valid LoginUserRequest request){
        TokenDto token = securityService.login(request);
        return ResponseEntity.ok(token);
    }
    @ApiOperation(value = "Token 재발급", notes = "Refresh Token 재발급")
    @PostMapping("/re-issue")
    public ResponseEntity<TokenDto> reissue(@RequestBody @ApiParam(value = "Refresh Token 재발급에 필요한 정보(access token, refresh token)") TokenRequest request) throws Exception {
        return ResponseEntity.ok(securityService.reissue(request));
    }
    @ApiOperation(value = "전체 회원 조회", notes = "전체 회원 조회")
    @GetMapping()
    public ResponseEntity<UserResponse.Result> findUsers(){
        List<User> findUsers = userService.findUsers();
        List<UserResponse> collect = findUsers.stream()
                .map(u -> new UserResponse(u.getId(),u.getName(),u.getEmail(),u.getPassword(),u.getImg(),u.getRole().name()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserResponse.Result(collect.size(), collect));
    }

    @ApiOperation(value = "특정 회원 조회", notes = "회원 id로 특정 회원 조회")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUser(@PathVariable("id") @ApiParam(value = "조회할 회원 id") Long id){
        User findUser = userService.findById(id);
        return ResponseEntity.ok(new UserResponse(findUser.getId(),findUser.getName(),findUser.getEmail(), findUser.getPassword(), findUser.getImg(),findUser.getRole().name()));
    }

    @ApiOperation(value = "회원 정보 수정",notes = "회원 id로 회원 검색 후 회원 정보 업데이트")
    @PatchMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody @ApiParam(value = "수정 할 회원 정보") UpdateUserRequest request, @PathVariable("id") @ApiParam(value = "수정할 회원 id") Long id,
                                                   @RequestParam(value = "img", required = false) @ApiParam(value = "수정 할 이미지 파일") MultipartFile file){
        User findUser = userService.findById(id);
        String name = request.getName()==null || request.getName().isBlank() ? findUser.getName():request.getName();
        String password = request.getPassword()==null || request.getPassword().isBlank() ?findUser.getPassword():request.getPassword();
        String img = request.getImg()==null || request.getImg().isBlank()?findUser.getImg(): request.getImg();
        UpdateUserRequest newUser = UpdateUserRequest.builder().name(name).password(password).img(img).build();
        userService.updateUser(newUser.toEntity(), id, file);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 삭제", notes="회원 id로 회원 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") @ApiParam(value = "삭젝할 회원 id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
