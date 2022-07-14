package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.user.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "글 작성 정보", description = "제목, 내용, 가격, 횟수, 회원 id")
public class CreatePostRequest {

    @NotEmpty
    @ApiModelProperty(value = "제목")
    private String title;

    @NotEmpty
    @ApiModelProperty(value = "내용")
    private String content;

    @NotNull
    @ApiModelProperty(value = "가격")
    private int price;

    @ApiModelProperty(value = "횟수")
    private String quantity;

    @ApiModelProperty(value = "회원 id")
    private Long userId;

    @Builder
    public CreatePostRequest(String title, String content, int price, String quantity, Long userId) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .price(price)
                .quantity(quantity)
                .build();
    }
}

