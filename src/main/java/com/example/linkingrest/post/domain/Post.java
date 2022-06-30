package com.example.linkingrest.post.domain;

import com.example.linkingrest.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int price;

    private String quantity;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(Long id, String title, String content, int price, String quantity, PostType postType, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.postType = postType;
        this.user = user;
    }

    public void setUser(User user){
        this.user = user;
        user.getPosts().add(this);
    }
    public static Post createPost(Post post, User user){
        post.setUser(user);
        //만약 user가 mentor이면 post type -> mentee, mentee이면 post type->mentor
        post.postType = PostType.FIND_MENTEE;
        return post;

    }
    public void updatePost(String title, String content, int price, String quantity) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
    }


}
