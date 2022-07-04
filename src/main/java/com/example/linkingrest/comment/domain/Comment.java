package com.example.linkingrest.comment.domain;

import com.example.linkingrest.post.domain.Post;
import com.example.linkingrest.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;

    private int rate;
    
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Comment(Long id, String content, int rate, LocalDateTime createdAt, User user, Post post) {
        this.id = id;
        this.content = content;
        this.rate = rate;
        this.createdAt = createdAt;
        this.user = user;
        this.post = post;
    }
    public void updateComment(String content, int rate){
        this.content = content;
        this.rate = rate;
    }

    public void setUser(User user){
        this.user = user;
        user.getComments().add(this);
    }
    public void setPost(Post post){
        this.post = post;
        post.getComments().add(this);
    }

}
