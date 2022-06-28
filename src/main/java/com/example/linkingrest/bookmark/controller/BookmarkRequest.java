package com.example.linkingrest.bookmark.controller;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.user.domain.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkRequest {

    private Long id;
    @NotNull
    private User user;
    @NotNull
    private Post post;

    @Builder
    public BookmarkRequest(Long id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public Bookmark toEntity(){
        return Bookmark.builder()
                .id(id)
                .user(user)
                .post(post)
                .build();

    }
}
