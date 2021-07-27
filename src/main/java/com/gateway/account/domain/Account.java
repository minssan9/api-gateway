package com.gateway.account.domain;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@RedisHash("account")
public class Account   {
    private  String username;
    private String password;
    private Set<String> authorities;
    private  boolean isExpired;

    public Account(String username, String password, Set<String> authorities, boolean isExpired) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isExpired = isExpired;
    }
}
