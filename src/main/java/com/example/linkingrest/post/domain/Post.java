package com.example.linkingrest.post.domain;

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

    @Builder
    public Post(Long id, String title, String content, int price, String quantity, PostType postType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.postType = postType;
    }
}
