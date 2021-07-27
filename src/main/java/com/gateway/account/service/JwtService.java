package com.gateway.account.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.config.properties.TokenInfoProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtService {

    @Autowired
    TokenInfoProperties tokenInfoProperties;
    @Autowired
    ObjectMapper objectMapper;

    public Map<String, Object> getClaimsFromJWT(String token) {
        Jws<Claims> claimsJws = null;
        claimsJws = Jwts.parser()
            .setSigningKey(tokenInfoProperties.getJwtKey().getBytes(StandardCharsets.UTF_8))
            .parseClaimsJws(token);

        if (validateExp(claimsJws))
            return  claimsJws.getBody();
        else
            return new HashMap<>();
    }


    public boolean validateExp(Jws<Claims> claimsJws) {
        try {
            Date exp = claimsJws.getBody().getExpiration();
            Date now = new Date();
            if(exp.after(now)){
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
}
