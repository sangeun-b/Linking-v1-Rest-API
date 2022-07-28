package com.example.linkingrest.post.domain;

import com.example.linkingrest.bookmark.domain.Bookmark;
import com.example.linkingrest.comment.domain.Comment;
import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Slf4j
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

    @OneToMany(mappedBy = "post")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

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
        log.info("저장된 롤, SAVED ROLE: ", user.getRole().getAuth());
        log.info("저장된 회원, SAVED USER: ", user);
        if(user.getRole().getAuth().equals("ROLE_MENTOR")){
            post.postType = PostType.FIND_MENTEE;
        } else if(user.getRole().getAuth().equals("ROLE_MENTEE")){
            post.postType = PostType.FIND_MENTOR;
        }
        return post;

    }
    public void updatePost(String title, String content, int price, String quantity) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
    }


}
