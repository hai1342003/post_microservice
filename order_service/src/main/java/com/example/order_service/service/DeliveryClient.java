package com.example.order_service.service;

import com.example.order_service.dto.DeliveryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "DELIVERY-SERVICE")
public interface DeliveryClient {
    @PostMapping("/api/deliveries")
    DeliveryDTO createDelivery(@RequestBody DeliveryDTO deliveryDTO);

}
