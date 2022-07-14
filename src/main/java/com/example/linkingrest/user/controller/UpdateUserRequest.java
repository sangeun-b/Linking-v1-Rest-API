package com.example.linkingrest.user.controller;

import com.example.linkingrest.user.domain.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "회원 수정 정보", description = "이름, 비밀번호, 이미지")
public class UpdateUserRequest {

    @ApiModelProperty(value = "이름")
    private String name;

    @ApiModelProperty(value = "비밀번호")
    private String password;

    @ApiModelProperty(value = "이미지")
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
