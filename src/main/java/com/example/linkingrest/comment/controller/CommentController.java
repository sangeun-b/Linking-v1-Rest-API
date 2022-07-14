package com.example.linkingrest.comment.controller;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.comment.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Api(tags = {"COMMENT API V1"})
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 작성", notes = "새로운 댓글 생성")
    @PostMapping("/new")
    public ResponseEntity<CreateCommentResponse> saveComment(@RequestBody @ApiParam(value = "댓글 작성에 필요한 정보") CreateCommentRequest request){
        Long id = commentService.saveComment(request.toEntity(),request.getUserId(),request.getPostId());
        return ResponseEntity.ok(new CreateCommentResponse(id));
    }
    @ApiOperation(value = "회원 별 댓글 조회", notes = "회원 id로 댓글 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<CommentResponse.Result> findCommentsByUser(@PathVariable("userId") @ApiParam(value = "댓글 조회할 회원 id") Long userId){
        List<Comment> findComments = commentService.findCommentsByUser(userId);
        List<CommentResponse> collect = findComments.stream()
                .map(c -> new CommentResponse(c.getId(),c.getContent(),c.getRate(),c.getCreatedAt(),c.getUser().getId(),c.getPost().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CommentResponse.Result(collect.size(),collect));
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글 내용 수정")
    @PatchMapping("/{id}")
    public ResponseEntity updateComment(@PathVariable("id") @ApiParam(value = "수정할 댓글 id") Long id, @RequestBody @ApiParam(value = "댓글 수정 내용") UpdateCommentRequest request){
        commentService.updateComment(request.toEntity(),id);
        Comment comment = commentService.findById(id);
        return ResponseEntity.ok(new CommentResponse(comment.getId(), comment.getContent(),comment.getRate(),comment.getCreatedAt(),comment.getUser().getId(),comment.getPost().getId()));

    }
    @ApiOperation(value = "댓글 삭제", notes = "id로 조회 후 댓글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteComment(@PathVariable("id") @ApiParam(value = "삭제할 댓글 id") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
