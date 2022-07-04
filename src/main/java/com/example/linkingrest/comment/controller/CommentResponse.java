package com.example.linkingrest.comment.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long id;

    private String content;

    private int rate;

    private LocalDateTime createdAt;

    private Long userId;

    private Long PostId;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T comments;

    }

}
