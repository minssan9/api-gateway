package com.gateway.account.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.gateway.account.domain.Account;
import com.gateway.account.domain.TokenInfo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class JwtValidator {
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    TokenInfo tokenInfo;
    @Autowired
    ObjectMapper objectMapper;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public Map<String, Object> getClaimsFromJWT(String token) {
        Jws<Claims> claimsJws = null;
        claimsJws = Jwts.parser()
                .setSigningKey(tokenInfo.getJwtKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token);

        if (validateExp(claimsJws))
            return claimsJws.getBody();
        else
            return new HashMap<>();
    }

    // Request의 Header에서 token 값을 가져옴. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(ServerWebExchange request) {
        return request.getRequest().getHeaders().get("X-AUTH-TOKEN").get(0);
//        return request.getRequest().getHeaders().get("Authorization").get(0);
    }

    public Account getUserParseInfo(String token) {
        Claims parseInfo = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        //expiration date < now
        boolean isExpired = !parseInfo.getExpiration().before(new Date());
        Account account = new Account(parseInfo.getSubject(), "password", parseInfo.get("role", Set.class), isExpired);
        return account;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken, String checkAuth) {
        boolean result = true;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
            if (!claims.getBody().getExpiration().before(new Date())) return false;

            if (claims.getBody().get("").equals("")) return false;

            return result;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateExp(Jws<Claims> claimsJws) {
        try {
            Date exp = claimsJws.getBody().getExpiration();
            Date now = new Date();
            if (exp.after(now)) {
                return true;
            }
            return false;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
}
