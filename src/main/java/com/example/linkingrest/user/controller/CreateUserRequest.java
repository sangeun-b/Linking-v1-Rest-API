package com.example.linkingrest.user.controller;

import com.example.linkingrest.user.domain.Role;
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

    private Role role;

    @Builder
    public CreateUserRequest(String name, String email, String password, String img, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.img = img;
        this.role = role;
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .img(img)
                .role(role)
                .build();
    }
}
