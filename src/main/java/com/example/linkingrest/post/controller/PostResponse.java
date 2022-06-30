package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private int price;

    private String quantity;

    private PostType postType;

    private Long userId;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T posts;

    }
    }
