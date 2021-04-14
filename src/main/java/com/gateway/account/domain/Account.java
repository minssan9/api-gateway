package com.gateway.account.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Builder
@RedisHash("account")
public class Account   {

    @Id
    private  String username;
    private String password;
    private  Set<GrantedAuthority> authorities;
    private  boolean isExpired;

    public Account(String username, String password, Set<GrantedAuthority> authorities, boolean isExpired) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isExpired = isExpired;
    }
}
