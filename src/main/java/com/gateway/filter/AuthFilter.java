package com.gateway.filter;


import com.gateway.account.repository.TokenInfoRepository;
import com.gateway.account.service.JwtValidator;
import com.gateway.config.exception.CommonException;
import com.gateway.config.exception.CommonExceptionType;
import com.gateway.filter.AuthFilter.Config;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<Config> {
    private static final Logger logger = LogManager.getLogger(AuthFilter.class);
    public AuthFilter() {
        super(Config.class);
    }

    @Autowired
    JwtValidator jwtValidator;
    @Autowired
    TokenInfoRepository tokenInfoRepository;
    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public GatewayFilter apply(Config config) {

        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();


        return ((exchange, chain) -> {
            logger.info("ERPFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                logger.info("ERPFilter Start>>>>>>" + exchange.getRequest());
            }
            String path = exchange.getRequest().getPath().toString();
            String accountId = exchange.getRequest().getHeaders().get("username").toString();
            String accessToken = exchange.getRequest().getHeaders().get("Authorization").toString();

            if ( jwtValidator.getClaimsFromJWT(accessToken).isEmpty()) {
                throw new CommonException(CommonExceptionType.TOKEN_EXPIRED);
            };

            if ( exchange.getRequest().getPath().equals("/logout")) {
                tokenInfoRepository.deleteById(accessToken );
//                valueOperations.set(newAccessToken, newAccessToken);
            }



            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("ERPFilter End>>>>>>" + exchange.getResponse());
                }
                if (path.equals("/token/object")) {
                    String newAccessToken = exchange.getResponse().getHeaders().get("Authorization").toString();
                    valueOperations.set(newAccessToken, newAccessToken);

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
