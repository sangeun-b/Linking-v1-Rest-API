package com.example.linkingrest.comment.controller;

import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.user.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "댓글 작성 정보", description = "내용, 평점, 작성날짜, 회원 id, 글 id")
public class CreateCommentRequest {

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "평점")
    private int rate;

    @ApiModelProperty(value = "작성날짜")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "회원 id")
    private Long userId;

    @ApiModelProperty(value = "글 id")
    private Long postId;

    @Builder
    public CreateCommentRequest(String content, int rate, LocalDateTime createdAt, Long userId, Long postId) {
        this.content = content;
        this.rate = rate;
        this.createdAt = createdAt;
        this.userId = userId;
        this.postId = postId;
    }

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .rate(rate)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
