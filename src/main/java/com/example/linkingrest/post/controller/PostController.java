package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.service.PostService;
import com.example.linkingrest.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable("id") Long id){
        Post post = postService.findPostById(id);
        return ResponseEntity.ok(new PostResponse(post.getId(),post.getTitle(),post.getContent(),post.getPrice(),post.getQuantity(),post.getPostType(),post.getUser().getId()));
    }

    @GetMapping("/{type}")
    public ResponseEntity<PostResponse.Result> findByType(@PathVariable("type") String type) {
        List<Post> findPosts = postService.findPostsByType(type);
        List<PostResponse> collect = findPosts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getPrice(), p.getQuantity(), p.getPostType(), p.getUser().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PostResponse.Result(collect.size(),collect));

    }
    @PatchMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable("id") Long id, @RequestBody UpdatePostRequest request){
        Post findPost = postService.findPostById(id);
//        String title = request.getTitle() == null ? findPost.getTitle():request.getTitle();
//        String content = request.getContent() == null? findPost.getContent() : request.getContent();
//        Integer price = request.getPrice()==null? findPost.getPrice() : request.getPrice();
        postService.updatePost(request.toEntity(), id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PostResponse.Result> findByKeyword(@RequestParam(value = "keyword", required = false) String keyword){
        List<Post> findPosts = postService.findPostByKeyword(keyword);
        List<PostResponse> collect = findPosts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getPrice(), p.getQuantity(), p.getPostType(), p.getUser().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PostResponse.Result(collect.size(),collect));
    }

    }
