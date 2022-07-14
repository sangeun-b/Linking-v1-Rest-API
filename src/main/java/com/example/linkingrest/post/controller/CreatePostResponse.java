package com.example.linkingrest.post.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel(value = "댓글 작성 응답", description = "id")
public class CreatePostResponse {

    @ApiModelProperty(value = "id")
    private Long id;
}
