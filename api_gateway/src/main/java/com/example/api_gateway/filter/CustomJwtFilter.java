//package com.example.apigateway.filter;
//
//import com.example.user_service.security.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.ServerWebInputException;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CustomJwtFilter implements GatewayFilter {
//
//    private final JwtService jwtService;
//
//    @Autowired
//    public CustomJwtFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//
//        // Lấy Authorization Header
//        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            // Nếu không có token hoặc không đúng định dạng, từ chối request
//            return chain.filter(exchange);
//        }
//
//        try {
//            String token = authHeader.substring(7); // Bỏ chữ "Bearer "
//            String username = jwtService.extractUsername(token); // Lấy thông tin từ token
//
//            if (jwtService.isTokenValid(token)) {
//                // Thêm thông tin xác thực vào header cho request
//                exchange = exchange.mutate()
//                        .request(r -> r.headers(headersConsumer -> {
//                            headersConsumer.add("X-Authenticated-User", username);
//                        }))
//                        .build();
//            }
//        } catch (Exception e) {
//            // Nếu token không hợp lệ, từ chối request
//            throw new ServerWebInputException("Invalid JWT Token");
//        }
//
//        return chain.filter(exchange);
//    }
//}
