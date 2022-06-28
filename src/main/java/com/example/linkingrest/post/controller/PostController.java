package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.post.service.PostService;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/new/{userId}")
    public ResponseEntity<CreatePostResponse> savePost(@PathVariable("userId") Long userId, @RequestBody CreatePostRequest request){
        Long id = postService.savePost(request.toEntity(),userId);
        return ResponseEntity.ok(new CreatePostResponse(id));

    }
}
