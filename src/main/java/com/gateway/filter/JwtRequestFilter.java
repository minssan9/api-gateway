package com.gateway.filter;

import static com.gateway.config.properties.StaticProperties.AUTH_SERVER_URL;

import com.gateway.domain.Account;
import com.gateway.domain.LoginInfo;
import com.gateway.service.JwtValidator;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtRequestFilter extends
    AbstractGatewayFilterFactory<JwtRequestFilter.Config> implements Ordered {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private JwtValidator jwtValidator;
    @Autowired
    Gson gson;
    @Autowired
    WebClient webClient;

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

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    public JwtRequestFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            try {
                String token = exchange.getRequest().getHeaders().get("Authorization").get(0)
                    .substring(7);

                Map<String, String> userMap = gson.fromJson(exchange.getRequest().getBody().toString(),
                    new HashMap<String, String>().getClass());

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

                    Account account = webClient
                        .post()
                        .uri("/oauth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(loginInfo)
                        .retrieve()
                        .bodyToMono(Account.class)
                        .block();

                    Map<String, Object> userInfo = jwtValidator.getUserParseInfo(token);
                    ArrayList<String> arr = (ArrayList<String>) userInfo.get("role");
                    if (!arr.contains(config.getRole())) {
                        throw new IllegalArgumentException();
                    }
                    //generate Token and save in redis
                    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
                    vop.set(account.getId(), account.);
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
