package com.example.linkingrest.user.controller;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private String img;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T user;

    }
}
