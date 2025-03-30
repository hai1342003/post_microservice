package com.example.apigateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final WebClient webClient;

    public JwtAuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl("http://localhost:8085").build();
        System.out.println("✅ [API Gateway] JwtAuthenticationFilter đã được Spring khởi tạo!");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            System.out.println("📢 [API Gateway] Nhận request: " + exchange.getRequest().getURI());

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("❌ [API Gateway] Không tìm thấy header Authorization!");
                return unauthorizedResponse(exchange);
            }

            String token = authHeader.substring(7);
            System.out.println("✅ [API Gateway] Nhận token: " + token);

            return webClient.get()
                    .uri("/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> System.out.println("✅ [API Gateway] Token hợp lệ: " + response))
                    .doOnError(error -> System.out.println("❌ [API Gateway] Token không hợp lệ hoặc lỗi xác thực: " + error.getMessage()))
                    .flatMap(response -> {
                        ServerHttpRequest modifiedRequest = exchange.getRequest()
                                .mutate()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build();
                        System.out.println("🚀 [API Gateway] Forward request với Authorization header!");
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(error -> {
                        System.out.println("❌ [API Gateway] Xác thực thất bại! Lỗi: " + error.getMessage());
                        error.printStackTrace();
                        return unauthorizedResponse(exchange);
                    });
        };
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        System.out.println("🔒 [API Gateway] Trả về 401 Unauthorized!");
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}
