package com.example.linkingrest.post.controller;

import com.example.linkingrest.post.domain.Post;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "글 수정 정보", description = "제목, 내용, 가격, 횟수")
public class UpdatePostRequest {

    @NotEmpty
    @ApiModelProperty(value = "제목")
    private String title;

    @NotEmpty
    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "가격")
    private int price;

    @ApiModelProperty(value = "횟수")
    private String quantity;

    @Builder
    public UpdatePostRequest(String title, String content, int price, String quantity) {
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

