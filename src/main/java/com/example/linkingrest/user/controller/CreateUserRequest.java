package com.example.linkingrest.user.controller;

import com.example.linkingrest.user.domain.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@EqualsAndHashCode
public class CreateUserRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String img;

    @Builder
    public CreateUserRequest(String name, String email, String password, String img) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.img = img;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .img(img)
                .build();
    }
}
