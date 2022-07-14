package com.example.linkingrest.comment.controller;

import com.example.linkingrest.comment.domain.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "댓글 수정 정보", description = "내용, 평점")
public class UpdateCommentRequest {

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "평점")
    private int rate;

    @Builder
    public UpdateCommentRequest(String content, int rate) {
        this.content = content;
        this.rate = rate;
    }

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .rate(rate)
                .build();
    }
}
