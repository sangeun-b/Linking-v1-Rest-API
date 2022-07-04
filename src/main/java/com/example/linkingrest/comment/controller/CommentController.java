package com.example.linkingrest.comment.controller;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/new/user/{userId}/post/{postId}")
    public ResponseEntity<CreateCommentResponse> saveComment(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId, @RequestBody CreateCommentRequest request){
        Long id = commentService.saveComment(request.toEntity(),userId, postId);
        return ResponseEntity.ok(new CreateCommentResponse(id));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CommentResponse.Result> findCommentsByUser(@PathVariable("userId") Long userId){
        List<Comment> findComments = commentService.findCommentsByUser(userId);
        List<CommentResponse> collect = findComments.stream()
                .map(c -> new CommentResponse(c.getId(),c.getContent(),c.getRate(),c.getCreatedAt(),c.getUser().getId(),c.getPost().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CommentResponse.Result(collect.size(),collect));
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable("id") Long id, @RequestBody UpdateCommentRequest request){
        commentService.updateComment(request.toEntity(),id);
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(new CommentResponse(comment.getId(), comment.getContent(),comment.getRate(),comment.getCreatedAt(),comment.getUser().getId(),comment.getPost().getId()));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable("id") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
