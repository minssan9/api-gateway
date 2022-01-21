package com.gateway.filter;


import com.gateway.filter.AuthFilter.Config;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<Config> {
    private static final Logger logger = LogManager.getLogger(AuthFilter.class);
    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            logger.info("ERPFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                logger.info("ERPFilter Start>>>>>>" + exchange.getRequest());
            }

            String path = exchange.getRequest().getPath().toString();
            String accountId = exchange.getRequest().getHeaders().get("username").toString();
            String accessToken = exchange.getRequest().getHeaders().get("Authorization").toString();

            if ( exchange.getRequest().getPath().equals("/logout")) {
            //                valueOperations.set(newAccessToken, newAccessToken);
            }



            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("ERPFilter End>>>>>>" + exchange.getResponse());
                }
                if (path.equals("/token/object")) {
                    String newAccessToken = exchange.getResponse().getHeaders().get("Authorization").toString();
//                    valueOperations.set(newAccessToken, newAccessToken);
                }
            }));
        });
    }


    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
