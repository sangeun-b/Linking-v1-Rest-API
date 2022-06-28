package com.example.linkingrest.bookmark.controller;

import com.example.linkingrest.bookmark.domain.Bookmark;
import lombok.*;

@Getter
@Setter
public class BookmarkResponse {

    private Long id;
    private Long userId;
    private Long postId;

    public BookmarkResponse(Long id){
        this.id = id;
    }

    @Builder
    public BookmarkResponse(Long id, Long userId, Long postId) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
    }
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T places;
    }

}
