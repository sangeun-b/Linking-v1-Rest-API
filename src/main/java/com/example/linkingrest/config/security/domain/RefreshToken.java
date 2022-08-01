package com.example.linkingrest.config.security.domain;

import com.example.linkingrest.user.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "REFRESH_TOKEN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 객체를 가져오기 위한 용도로 key 값 생성, user를 특정할 수 없는 userPK값을 key값으로 지정
    @Column(name = "user_key")
    private Long key;

    private String token;

    public RefreshToken updateToken(String token){
        this.token = token;
        return this;
    }

    @Builder
    public RefreshToken(Long key, String token){
        this.key = key;
        this.token = token;
    }
}
