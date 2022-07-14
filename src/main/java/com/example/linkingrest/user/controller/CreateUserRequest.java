package com.example.linkingrest.user.controller;

import com.example.linkingrest.user.domain.Role;
import com.example.linkingrest.user.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@EqualsAndHashCode
@ApiModel(value = "새 회원 생성 정보", description = "이름, 이메일, 비밀번호, 이미지, 역할")
public class CreateUserRequest {

    @NotEmpty
    @ApiModelProperty(value = "이름")
    private String name;

    @NotEmpty
    @ApiModelProperty(value = "이메일")
    private String email;

    @NotEmpty
    @ApiModelProperty(value = "비밀번호")
    private String password;

    @ApiModelProperty(value = "이미지 url")
    private String img;

    @ApiModelProperty(value = "역할")
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
