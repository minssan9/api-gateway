package com.gateway.route;

import com.gateway.jwt.JwtRequestFilter;
import com.gateway.jwt.JwtRequestFilter.Config;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseRouteLocator  {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, JwtRequestFilter jwtFilter) {
        return builder.routes()
            .route("path_route", r->r.path("/oauth/token")
                    .filters(f->f.addRequestHeader("Hello","World")
                    .rewritePath("/oauth/token", "/oauth/token"))
                    .uri("http://localhost:34009/"))
            .route("api_route", r -> r.path("/api")
                .filters(f->f
                    .filter(jwtFilter.apply(new JwtRequestFilter.Config("dummy", true, false)))
                    .addRequestHeader("Hello", "World")
                .rewritePath("/new", "/"))
                .uri("http://localhost:34000"))
            .route("wms_route", r -> r.host("/wms")
                .uri("http://localhost:34001"))
            .route("rewrite_route", r -> r
                .host("10.30.71.156")
                .filters(f -> f
                    .rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                .uri("http://httpbin.org"))
            .route(p -> p
                .host("*.example.com")
                .filters(f -> f.hystrix(config -> config
                    .setName("mycmd")
                    .setFallbackUri("forward:/fallback")))
                .uri("http://httpbin.org:80"))
            .build();
    }
}
