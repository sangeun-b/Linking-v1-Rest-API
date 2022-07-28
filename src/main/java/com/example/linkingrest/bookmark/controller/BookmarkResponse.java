package com.example.linkingrest.bookmark.controller;

import com.example.linkingrest.bookmark.domain.Bookmark;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ApiModel(value = "BOOKMARK 정보", description = "bookmark id, user id, post id 정보 가지고 있음")
public class BookmarkResponse {

    @ApiModelProperty(value = "bookmark id")
    private Long id;
    @ApiModelProperty(value = "user id")
    private Long userId;
    @ApiModelProperty(value = "post id")
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
        private int count;
        private T bookmarks;
    }

}
