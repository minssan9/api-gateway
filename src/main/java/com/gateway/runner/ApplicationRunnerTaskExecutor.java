package com.gateway.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Slf4j
@Component
public class ApplicationRunnerTaskExecutor implements ApplicationRunner {
    @Value("${spring.application.name}")
    String applicationName ;

    @Override
    public void run(ApplicationArguments args)  {

        log.info(applicationName);
    }
}
