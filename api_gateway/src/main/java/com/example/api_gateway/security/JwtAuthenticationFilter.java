package com.example.api_gateway.security;

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

    public JwtAuthenticationFilter() {
        super(Config.class);
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8085") // Gọi trực tiếp auth-service
                .build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorizedResponse(exchange);
            }

            String token = authHeader.substring(7);

            return webClient.get()
                    .uri("/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(status -> status.isError(), res -> Mono.error(new RuntimeException("Invalid token")))
                    .toBodilessEntity() // ⬅️ Chỉ check status, không đọc body
                    .doOnSuccess(response -> System.out.println("✅ [API Gateway] Token hợp lệ: " + response))
                    .doOnError(error -> System.out.println("❌ [API Gateway] Token không hợp lệ hoặc lỗi xác thực: " + error.getMessage()))
                    .flatMap(response -> {
                        System.out.println("✅ Vượt qua xác thực, tiếp tục forward sang service");
                        ServerHttpRequest modifiedRequest = exchange.getRequest()
                                .mutate()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(error -> {
                        error.printStackTrace();
                        return unauthorizedResponse(exchange);
                    });
        };
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}
