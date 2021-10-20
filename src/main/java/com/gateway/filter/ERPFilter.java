package com.gateway.filter;


import com.gateway.account.repository.TokenInfoRepository;
import com.gateway.account.service.JwtValidator;
import com.gateway.config.exception.CommonException;
import com.gateway.config.exception.CommonExceptionType;
import com.gateway.filter.ERPFilter.Config;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ERPFilter extends AbstractGatewayFilterFactory<Config> {
    private static final Logger logger = LogManager.getLogger(ERPFilter.class);
    public ERPFilter() {
        super(Config.class);
    }


    @Autowired
    JwtValidator jwtValidator;
    @Autowired
    TokenInfoRepository tokenInfoRepository;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            logger.info("ERPFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                logger.info("ERPFilter Start>>>>>>" + exchange.getRequest());
            }


//            Access Token 있는지 확인
//            String accessToken = exchange.getRequest().getHeaders().get("Authorization").toString();
//            if ( jwtValidator.getClaimsFromJWT(accessToken).isEmpty()) {
//                throw new CommonException(CommonExceptionType.TOKEN_EXPIRED);
//            };




            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("ERPFilter End>>>>>>" + exchange.getResponse());
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
