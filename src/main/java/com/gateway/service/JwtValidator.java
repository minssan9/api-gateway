package com.gateway.service;


import com.gateway.account.domain.Account;
import com.gateway.account.redis.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.Serializable;
import java.util.*;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtValidator  {
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private AccountRepository accountRepository ;

    public Account getUserParseInfo(String token) {
        Claims parseInfo = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        //expiration date < now
        boolean isExpired = !parseInfo.getExpiration().before(new Date());

        Account account = new Account(parseInfo.getSubject(), "password", parseInfo.get("role", Set.class), isExpired );
        return account;
    }

    // Request의 Header에서 token 값을 가져옴. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }


    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken, String checkAuth) {
        boolean result = true;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);

            Account account = accountRepository.findByUsername(claims.getBody().get("username").toString()) ;
            if ( !claims.getBody().getExpiration().before(new Date())) return false;

            if (claims.getBody().getget("authoritiesqq      1qqqq")checkAuth ) return false;

            return result;
        } catch (Exception e) {
            return false;
        }
    }
}
