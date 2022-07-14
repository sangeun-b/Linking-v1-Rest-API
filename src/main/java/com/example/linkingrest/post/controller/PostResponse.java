package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.PostType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "글 정보", description = "id, 제목, 내용, 가격, 횟수, 글 카테고리, 회원 id")
public class PostResponse {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "가격")
    private int price;

    @ApiModelProperty(value = "횟수")
    private String quantity;

    @ApiModelProperty(value = "글 카테고리")
    private PostType postType;

    @ApiModelProperty(value = "회원 id")
    private Long userId;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T posts;

    }
    }
