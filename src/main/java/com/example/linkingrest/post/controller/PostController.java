package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.service.PostService;
import com.example.linkingrest.user.controller.CreateUserResponse;
import com.example.linkingrest.user.domain.User;
import com.example.linkingrest.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Api(tags = {"POST API V1"})
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @ApiOperation(value = "글 작성", notes = "새 글 작성")
    @PostMapping("/new")
    public ResponseEntity<CreatePostResponse> savePost(@RequestBody @ApiParam(value = "댓글 작성 내용") CreatePostRequest request){
        Long id = postService.savePost(request.toEntity(),request.getUserId());
        return ResponseEntity.created(URI.create("/posts/"+id)).body(new CreatePostResponse(id));

    }
    @ApiOperation(value = "글 조회", notes = "글 id로 조회")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable("id") @ApiParam(value = "조회 할 글 id") Long id){
        Post post = postService.findPostById(id);
        return ResponseEntity.ok(new PostResponse(post.getId(),post.getTitle(),post.getContent(),post.getPrice(),post.getQuantity(),post.getPostType(),post.getUser().getId()));
    }

    @ApiOperation(value = "카테고리로 조회", notes = "Mentor/Mentee 카테고리로 조회")
    @GetMapping("type/{type}")
    public ResponseEntity<PostResponse.Result> findByType(@PathVariable("type") @ApiParam(value = "조회할 카테고리") String type) {
        List<Post> findPosts = postService.findPostsByType(type);
        List<PostResponse> collect = findPosts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getPrice(), p.getQuantity(), p.getPostType(), p.getUser().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PostResponse.Result(collect.size(),collect));

    }
    @ApiOperation(value = "글 수정", notes = "작성된 글 수정")
    @PatchMapping("/{id}")
    public ResponseEntity updatePost(@PathVariable("id") @ApiParam(value = "수정할 글 id") Long id, @RequestBody @ApiParam(value = "수정할 글 내용") UpdatePostRequest request){

//        String title = request.getTitle() == null ? findPost.getTitle():request.getTitle();
//        String content = request.getContent() == null? findPost.getContent() : request.getContent();
//        Integer price = request.getPrice()==null? findPost.getPrice() : request.getPrice();
        postService.updatePost(request.toEntity(), id);
        Post findPost = postService.findPostById(id);
        return ResponseEntity.ok().build();
    }
    @ApiOperation(value = "글 삭제", notes = "작성된 글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable("id") @ApiParam(value = "삭제할 글 id") Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "글 검색", notes = "키워드로 글 검색")
    @GetMapping
    public ResponseEntity<PostResponse.Result> findByKeyword(@RequestParam(value = "keyword", required = false) @ApiParam(value = "조회할 키워드") String keyword){
        List<Post> findPosts = postService.findPostByKeyword(keyword);
        List<PostResponse> collect = findPosts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getPrice(), p.getQuantity(), p.getPostType(), p.getUser().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PostResponse.Result(collect.size(),collect));
    }

    }
