package com.example.linkingrest.config.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;

    @Builder
    public TokenDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpireDate) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
    }
}
