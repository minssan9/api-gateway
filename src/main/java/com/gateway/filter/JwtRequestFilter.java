package com.gateway.filter;

import com.gateway.account.LoginInfo;
import com.gateway.account.domain.Account;
import com.gateway.account.service.JwtValidator;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.gateway.config.properties.StaticProperties.AUTH_SERVER_URL;

@Component
@Slf4j
public class JwtRequestFilter extends
    AbstractGatewayFilterFactory<JwtRequestFilter.Config> implements Ordered {

    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    private Gson gson;

    @Override
    public int getOrder() {
        return -2; // -1 is response write filter, must be called before that
    }

    public static class Config {
        private String role;
        public Config(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    @Bean
    public ErrorWebExceptionHandler myExceptionHandler() {
        return new MyWebExceptionHandler();
    }

    public class MyWebExceptionHandler implements ErrorWebExceptionHandler {
        private String errorCodeMaker(int errorCode) {
            return "{\"errorCode\":" + errorCode + "}";
        }

        @Override
        public Mono<Void> handle(
            ServerWebExchange exchange, Throwable ex) {
            log.warn("in GATEWAY Exeptionhandler : " + ex);
            int errorCode = 999;
            if (ex.getClass() == NullPointerException.class) {
                errorCode = 61;
            } else if (ex.getClass() == ExpiredJwtException.class) {
                errorCode = 56;
            } else if (ex.getClass() == MalformedJwtException.class
                || ex.getClass() == SignatureException.class
                || ex.getClass() == UnsupportedJwtException.class) {
                errorCode = 55;
            } else if (ex.getClass() == IllegalArgumentException.class) {
                errorCode = 51;
            }

            byte[] bytes = errorCodeMaker(errorCode).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }
    }

    public JwtRequestFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            try {
                String token =  jwtValidator.resolveToken(exchange);

                Map<String, String> userMap = gson.fromJson(
                        exchange.getRequest().getBody().toString(),
                        new HashMap<String, String>().getClass()
                );

                final LoginInfo loginInfo = LoginInfo.builder()
                    .username(userMap.get("username"))
                    .password(userMap.get("password"))
                    .build();

                log.info("test input loginInfo: " + loginInfo);

                if (token.equals("")) {
                    WebClient webClient = WebClient.builder()
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .baseUrl(AUTH_SERVER_URL)
                        .build();

//                    Account account = webClient
//                        .post()
//                        .uri("/token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .bodyValue(loginInfo)
//                        .retrieve()
//                        .bodyToMono(Account.class)
//                        .block();

                    Account userInfo = jwtValidator.getUserParseInfo(token);
                    Set<String> authorities =  userInfo.getAuthorities();

                    if (!authorities.contains(config.getRole())) {
                        throw new IllegalArgumentException();
                    }
                }
            } catch (NullPointerException e) {
                log.warn("no token.");

                exchange.getResponse().setStatusCode(HttpStatus.valueOf(401));

                log.info("status code : " + exchange.getResponse().getStatusCode());
                return chain.filter(exchange);
            }
            return chain.filter(exchange);
        });
    }
}
