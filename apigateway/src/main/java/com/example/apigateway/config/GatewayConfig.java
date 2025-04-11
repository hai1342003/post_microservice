//package com.example.apigateway.config;
//
//import com.example.apigateway.filter.GlobalFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("user_service", r -> r.path("/api/users/**")
//                        .uri("http://localhost:8085"))
//                .route("delivery_service", r -> r.path("/api/deliveries/**")
//                        .uri("http://localhost:8081"))
//                .route("product_service", r -> r.path("/api/products/**")
//                        .uri("http://localhost:8082"))
//                .route("payment_service", r -> r.path("/api/payments/**")
//                        .uri("http://localhost:8083"))
//                .route("order_service", r -> r.path("/api/orders/**")
//                        .uri("http://localhost:8084"))
//                .build();
//    }
//}
