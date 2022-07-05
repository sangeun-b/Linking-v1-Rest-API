package com.example.linkingrest.user.controller;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class LoginUserRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
