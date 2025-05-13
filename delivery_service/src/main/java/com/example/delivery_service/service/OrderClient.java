package com.example.delivery_service.service;

import com.example.delivery_service.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {





    @PostMapping("/api/orders/status")
    ResponseEntity<?> updateOrderStatus(@RequestBody Map<String, String> request);

}
