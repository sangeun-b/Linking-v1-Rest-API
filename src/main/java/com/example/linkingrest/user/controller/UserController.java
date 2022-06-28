package com.example.linkingrest.user.controller;

import com.example.linkingrest.bookmark.controller.BookmarkResponse;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity<CreateUserResponse> saveUser(@RequestBody CreateUserRequest request){
        Long id = userService.join(request.toEntity());
        return ResponseEntity.ok(new CreateUserResponse(id));
    }

    @GetMapping("")
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
        if(request.getName().isEmpty()){
           request.setName(findUser.getName());
        }else if(request.getPassword().isEmpty()){
            request.setPassword(findUser.getPassword());
        }else if(file == null){
            request.setImg(findUser.getImg());
        }
        userService.updateUser(request.toEntity(), id, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
