package com.example.linkingrest.comment.controller;

import com.example.linkingrest.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequest {

    private String content;

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
