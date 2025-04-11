//package com.example.apigateway.config;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class AddAuthorizationHeaderFilter implements GlobalFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null) {
//            ServerWebExchange mutatedExchange = exchange.mutate()
//                    .request(r -> r.headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, authHeader)))
//                    .build();
//            return chain.filter(mutatedExchange);
//        }
//
//        return chain.filter(exchange);
//    }
//}
//
