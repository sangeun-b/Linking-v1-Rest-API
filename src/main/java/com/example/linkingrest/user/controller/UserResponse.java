package com.example.linkingrest.user.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ApiModel(value = "회원 정보", description = "id, 이름, 이메일, 비밀번호, 이미지, 역학")
public class UserResponse {

    @ApiModelProperty(value = "id")
    private Long id;

    @NotEmpty
    @ApiModelProperty(value = "이름")
    private String name;

    @NotEmpty
    @ApiModelProperty(value = "이메일")
    private String email;

    @NotEmpty
    @ApiModelProperty(value = "비밀번호")
    private String password;

    @ApiModelProperty(value = "이미지")
    private String img;

    @ApiModelProperty(value = "역할")
    private String role;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T user;

    }
}
