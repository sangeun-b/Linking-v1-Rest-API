package com.example.linkingrest.comment.controller;

import com.example.linkingrest.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCommentRequest {


    private String content;

    private int rate;

    private LocalDateTime createdAt;

    private Long userId;

    private Long postId;

    @Builder
    public CreateCommentRequest(String content, int rate, LocalDateTime createdAt, Long userId, Long postId) {
        this.content = content;
        this.rate = rate;
        this.createdAt = createdAt;
        this.userId = userId;
        this.postId = postId;
    }

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .rate(rate)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
