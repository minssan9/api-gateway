package com.gateway.route;

import com.gateway.filter.ERPFilter;
import com.gateway.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class RouteLocatorConfig {
//
//    @Autowired
//    JwtRequestFilter jwtRequestFilter;
//
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder ) {
//        return builder.routes()
//            .route("auth_route", r -> r
//                    .path("/api/auth")
//                    .filters(f->f
//                        .rewritePath("/auth", "/")
//                    )
//                    .uri("http://10.20.101.134:34009"))
//            .route("erp_route", r -> r
//                    .path("/api/erp/**")
//                    .filters(f->f
//                        .addRequestHeader("Hello","World")
//                        .filter(jwtRequestFilter.apply(new JwtRequestFilter.Config("")))
//                        .rewritePath("/api/erp", "/")
//                    )
//                    .uri("http://10.20.101.133:34000"))
//            .route("wms_route", r -> r.path("/api/wms/**")
//                .filters(f->f
//                    .rewritePath("/api/wms", "/"))
//                .uri("http://10.20.101.133:34100"))
//            .build();
//    }
//}
