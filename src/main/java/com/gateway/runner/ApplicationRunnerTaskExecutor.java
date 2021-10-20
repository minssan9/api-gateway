package com.gateway.runner;

import com.gateway.account.repository.TokenInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Profile("!test")
@Slf4j
@Component
public class ApplicationRunnerTaskExecutor implements ApplicationRunner {

    @Autowired
    private final TokenInfoRepository tokenInfoRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.application.name}")
    String applicationName ;

    public ApplicationRunnerTaskExecutor(TokenInfoRepository tokenInfoRepository) {
        this.tokenInfoRepository = tokenInfoRepository;
    }

    @Override
    public void run(ApplicationArguments args)  {

        log.info(applicationName);
    }
}
