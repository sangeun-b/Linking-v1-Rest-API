package com.example.linkingrest.user.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_MENTOR("ROLE_MENTOR","멘토"),
    ROLE_MENTEE("ROLE_MENTEE","멘티"),
    ROLE_ADMIN("ROLE_ADMIN","관리자");

    String auth;
    String desc;

    Role(String auth, String desc) {
        this.auth = auth;
        this.desc = desc;
    }
}
