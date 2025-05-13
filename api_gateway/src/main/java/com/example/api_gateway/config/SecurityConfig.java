package com.example.api_gateway.config;

//import com.example.apigateway.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//
//
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Tắt CSRF
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/**").permitAll() // Cho phép truy cập không cần auth
//                        .pathMatchers("api/deliveries/**").authenticated()
                        .pathMatchers("/actuator/**").permitAll() // Cho phép truy cập không cần auth

                        .anyExchange().permitAll()
                )
                .build();
    }
}
