package com.example.linkingrest.user.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "새 회원 id 정보", description = "id")
public class CreateUserResponse {
    @ApiModelProperty(value = "id")
    private Long id;

}
