package com.gateway.filter;

import java.util.Arrays;

import com.gateway.config.exception.CommonException;
import com.gateway.config.exception.CommonExceptionType;
import com.gateway.config.handler.GatewayExceptionHandler;
import com.gateway.config.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.gateway.filter.GlobalFilter.Config;

import lombok.Data;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<Config> {
    @Autowired    private AppProperties appProperties;

    public GlobalFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (config.isPreLogger()) {
                log.info("GlobalFilter Start>>>>>>"
                        + exchange.getRequest().getURI() + "/" + exchange.getRequest().getPath() + "/" + exchange.getRequest().getQueryParams());
            }

            String hostIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        	exchange.getResponse().getHeaders().setAccessControlExposeHeaders(Arrays.asList("content-disposition"));
            return chain.filter(exchange).then(Mono.fromRunnable(()->{}));
        };
    }


    @Bean
    public ErrorWebExceptionHandler myExceptionHandler() {
        return new GatewayExceptionHandler();
    }



    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
