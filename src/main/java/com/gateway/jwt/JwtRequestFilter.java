package com.gateway.jwt;

import com.gateway.jwt.JwtRequestFilter.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtRequestFilter extends
    AbstractGatewayFilterFactory<Config> {

    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

        public Config(String baseMessage, boolean preLogger, boolean postLogger) {
            this.baseMessage = baseMessage;
            this.preLogger = preLogger;
            this.postLogger = postLogger;
        }
    }
    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            try {
                String token = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
            } catch (NullPointerException e) {
                log.warn("no token.");
                exchange.getResponse().setStatusCode(HttpStatus.valueOf(401));
                log.info("status code :" + exchange.getResponse().getStatusCode());
                return chain.filter(exchange);
            }
            return chain.filter(exchange);
        };
    }
}
