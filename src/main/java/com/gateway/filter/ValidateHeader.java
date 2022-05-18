package com.gateway.filter;

import com.gateway.config.properties.AppProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.PostConstruct;
import java.security.Key;

@Slf4j
@RequiredArgsConstructor
public class ValidateHeader {
    private final AppProperties appProperties;
    private Key secretKey;

//    @PostConstruct
//    protected void init() {
//        byte[] keyBytes = Decoders.BASE64.decode(appProperties.getAuth().getJwtSecret());
//        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public boolean validateHost(ServerWebExchange exchange){
////            Token 검증 방식
////            String token = exchange.getRequest().getHeaders().get("Authorization").toString();
////            Boolean bVaild = validateToken(token);
//
//        String hostIp = exchange.getRequest().getRemoteAddress().getAddress().getAddress().toString();
//        if (appProperties.getHost().getAllowed().contains(hostIp)){
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


}
