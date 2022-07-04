package com.example.linkingrest.bookmark.domain;


import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @Builder
    public Bookmark(Long id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public void setUser(User user){
        this.user = user;
        user.getBookmarks().add(this);
    }
    public void setPost(Post post){
        this.post = post;
        post.getBookmarks().add(this);
    }
}
