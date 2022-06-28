package com.example.linkingrest.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    private String img;

    @Builder
    public User(Long id, String name, String email, String password, String img) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.img = img;
    }
    public void update(String name, String password, String img){
        this.name = name;
        this.password = password;
        this.img = img;
    }
}
