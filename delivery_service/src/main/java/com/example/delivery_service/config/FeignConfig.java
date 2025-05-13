package com.example.delivery_service.config;


import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String token = RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes
                    ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getHeader("Authorization")
                    : null;
            if (token != null) {
                requestTemplate.header("Authorization", token);
            }
        };
    }
}
