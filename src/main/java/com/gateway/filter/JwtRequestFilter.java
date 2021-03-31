package com.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtRequestFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            String token = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
        } catch (NullPointerException e){
            log.warn("no token.");
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(401));
            log.info("status code : " + exchange.getResponse().getStatusCode());
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }
}
