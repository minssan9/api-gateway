package com.gateway.account.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "custom")
public class TokenInfo {
    String clientId;
    String clientSecret;
    String jwtKey;
    int accessTokenValidTime;
    int refreshTokenValidTime;
    String[] scopes;
    String[] grantTypes;
}
