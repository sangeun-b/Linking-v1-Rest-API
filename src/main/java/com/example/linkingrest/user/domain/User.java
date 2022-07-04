package com.example.linkingrest.user.domain;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    private String img;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    public User(Long id, String name, String email, String password, String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.img = img;
    }

    public void updateUser(String name, String password, String img){
        this.name = name;
        this.password = password;
        this.img = img;
    }
}
