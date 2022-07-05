package com.example.linkingrest.user.controller;

import com.example.linkingrest.config.security.JwtProvider;
import com.example.linkingrest.config.security.dto.TokenDto;
import com.example.linkingrest.config.security.dto.TokenRequest;
import com.example.linkingrest.config.security.service.SecurityService;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import io.swagger.annotations.Api;
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

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> saveUser(@RequestBody @Valid CreateUserRequest request){
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

    @GetMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginUserRequest request){
        TokenDto token = securityService.login(request);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/re-issue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequest request) throws Exception {
        return ResponseEntity.ok(securityService.reissue(request));
    }
    @GetMapping()
    public ResponseEntity<UserResponse.Result> findUsers(){
        List<User> findUsers = userService.findUsers();
        List<UserResponse> collect = findUsers.stream()
                .map(u -> new UserResponse(u.getId(),u.getName(),u.getEmail(),u.getPassword(),u.getImg()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UserResponse.Result(collect.size(), collect));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUser(@PathVariable("id") Long id){
        User findUser = userService.findById(id);
        return ResponseEntity.ok(new UserResponse(findUser.getId(),findUser.getName(),findUser.getEmail(), findUser.getPassword(), findUser.getImg()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody UpdateUserRequest request, @PathVariable("id") Long id,
                                                   @RequestParam(value = "img", required = false) MultipartFile file){
        User findUser = userService.findById(id);
        String name = request.getName()==null || request.getName().isBlank() ? findUser.getName():request.getName();
        String password = request.getPassword()==null || request.getPassword().isBlank() ?findUser.getPassword():request.getPassword();
        String img = request.getImg()==null || request.getImg().isBlank()?findUser.getImg(): request.getImg();
        UpdateUserRequest newUser = UpdateUserRequest.builder().name(name).password(password).img(img).build();
        userService.updateUser(newUser.toEntity(), id, file);
        return ResponseEntity.ok("회원 수정 성공");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
