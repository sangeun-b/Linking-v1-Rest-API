package com.example.linkingrest.user.controller;

import com.example.linkingrest.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserRequest {

    private String name;

    private String password;

    private String img;

    @Builder
    public UpdateUserRequest(String name, String password, String img) {
        this.name = name;
        this.password = password;
        this.img = img;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .password(password)
                .img(img)
                .build();
    }
}
