package com.example.apigateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



@Component
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;

    public JwtAuthenticationGlobalFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8085").build();

        System.out.println("✅ JwtAuthenticationFilter đã được Spring khởi tạo!");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);


        System.out.println("📢 Received request: " + exchange.getRequest().getURI());
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Không nhận được Authorization Header");
            return unauthorizedResponse(exchange);
        }

        String token = authHeader.substring(7);
        System.out.println("✅ Nhận được token: " + token);

        return webClient.get()
                .uri("/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    System.out.println("✅ Token hợp lệ: " + response);
                    ServerHttpRequest modifiedRequest = exchange.getRequest()
                            .mutate()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                })
                .onErrorResume(error -> {
                    System.out.println("❌ Token không hợp lệ hoặc lỗi xác thực: " + error.getMessage());
                    error.printStackTrace(); // Thêm dòng này để thấy lỗi chi tiết
                    return unauthorizedResponse(exchange);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}

