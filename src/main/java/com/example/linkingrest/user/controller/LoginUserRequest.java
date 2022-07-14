package com.example.linkingrest.user.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@ApiModel(value = "로그인 정보", description = "이메일, 비밀번호")
public class LoginUserRequest {

    @NotEmpty
    @ApiModelProperty(value = "이메일")
    private String email;

    @NotEmpty
    @ApiModelProperty(value = "비밀번호")
    private String password;
}
