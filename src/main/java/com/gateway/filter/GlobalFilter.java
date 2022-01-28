package com.gateway.filter;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.gateway.filter.GlobalFilter.Config;

import lombok.Data;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<Config> {
    private static final Logger logger = LogManager.getLogger(GlobalFilter.class);
    public GlobalFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (config.isPreLogger()) {
                logger.info("GlobalFilter Start>>>>>>" + exchange.getRequest().getURI());
                logger.info("GlobalFilter Start>>>>>>" + exchange.getRequest().getPath());
                logger.info("GlobalFilter Start>>>>>>" + exchange.getRequest().getQueryParams());
            }

            if(exchange.getRequest().getURI().getPath().equals("api/adm/intf/process/returnKey")){
                exchange.getResponse().setStatusCode(HttpStatus.OK);
            }

        	exchange.getResponse().getHeaders().setAccessControlExposeHeaders(Arrays.asList("content-disposition"));
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("GlobalFilter End>>>>>>" + exchange.getResponse().getStatusCode());
                }
            }));
        };
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
