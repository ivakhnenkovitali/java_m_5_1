package com.hrm.cloud.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/alpha/**")
                        .uri("lb://service-alpha"))
                .route(p -> p
                        .path("/bravo/**")
                        .uri("lb://service-bravo"))
                .build();
    }
}
