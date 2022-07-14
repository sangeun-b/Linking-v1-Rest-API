package com.example.linkingrest.user.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(value = "로그인 응답 정보", description = "이메일, 토큰")
public class LoginUserResponse {
    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = "토큰")
    private String token;
}
