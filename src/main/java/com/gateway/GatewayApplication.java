package com.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("api_route", r -> r
                .path("/api")
                .uri("http://10.20.101.181:34000"))

            .route("wms_route", r -> r.host("/wms")
                .uri("http://localhost:34001"))

            .route("rewrite_route", r -> r
                .host("10.30.71.156")
                .filters(f -> f
                    .rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                .uri("http://httpbin.org"))

            .build();
    }
}
