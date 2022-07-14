package com.example.linkingrest.comment.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(value = "댓글 작성 후 응답 정보", description = "id")
public class CreateCommentResponse {

    @ApiModelProperty(value = "id")
    private Long id;
}
