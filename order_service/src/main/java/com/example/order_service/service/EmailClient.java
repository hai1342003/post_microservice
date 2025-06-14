package com.example.order_service.service;


import com.example.order_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.order_service.dto.OrderEmailRequest;

@FeignClient(name = "EMAIL-SERVICE", configuration = FeignConfig.class)
public interface EmailClient {

    @PostMapping("/api/email/send-order-email")
    void sendOrderEmail(@RequestBody OrderEmailRequest request);
}
