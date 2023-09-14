package com.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public CommandLineRunner openApiGroups(
            RouteDefinitionLocator locator,
            SwaggerUiConfigParameters swaggerUiParameters) {
        return args -> locator
                .getRouteDefinitions().collectList().block()
                .stream()
                .map(RouteDefinition::getId)
                .forEach(swaggerUiParameters::addGroup);

//        return args -> locator
//                .getRouteDefinitions().collectList().block()
//                .stream()
//                .map(RouteDefinition::getId)
//                .filter(id -> id.matches(".*-SERVICE"))
//                .map(id -> id.replace("-SERVICE", ""))
//                .forEach(swaggerUiParameters::addGroup);
    }

//    @Bean
//    public OpenAPI springOpenAPI(
//            @Value("${openapi.service.title}") String serviceTitle,
//            @Value("${openapi.service.version}") String serviceVersion,
//            @Value("${openapi.service.url}") String url
//    ) {
//        return new OpenAPI()
//                .servers(List.of(new Server().url(url)))
//                .info(new Info().title(appName)
//                        .description(appName + " API 명세서")
//                        .version("v0.0.1"));
//    }
}
