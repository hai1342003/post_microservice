//package com.example.apigateway.config;
//
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class GlobalFiltersConfig {
//    @Bean
//    public GlobalFilter customGlobalFilter() {
//        return (exchange, chain) -> {
//            return chain.filter(exchange.mutate()
//                    .request(exchange.getRequest().mutate()
//                            .headers(httpHeaders -> {
//                                // Giữ lại header Authorization
//                                if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                                    httpHeaders.set(HttpHeaders.AUTHORIZATION,
//                                            exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
//                                }
//                            })
//                            .build())
//                    .build());
//        };
//    }
//}
