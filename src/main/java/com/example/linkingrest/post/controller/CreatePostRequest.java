package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.post.domain.PostType;
import com.example.linkingrest.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreatePostRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private int price;

    private String quantity;

    @Builder
    public CreatePostRequest(String title, String content, int price, String quantity) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
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

