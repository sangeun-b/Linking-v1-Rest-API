package com.example.linkingrest.config.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {

    String accessToken;
    String refreshToken;

    @Builder
    public TokenRequest(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
