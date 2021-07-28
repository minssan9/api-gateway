package com.gateway.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@RedisHash("tokenInfo")
public class TokenInfo {
    @Id
    private  String id;
    private  String token;

    public TokenInfo(String id, String token) {
        this.id = id ;
        this.token = token;
    }
}
