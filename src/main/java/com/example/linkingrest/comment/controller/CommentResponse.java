package com.example.linkingrest.comment.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ApiModel(value = "댓글 정보", description = "id, 내용, 평점, 작성날짜, 회원 id, 글 id")
public class CommentResponse {

    @ApiModelProperty(value = "id")
    private Long id;

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

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T comments;

    }

}
