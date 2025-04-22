//package com.example.order_service.service;
//
//import com.example.order_service.config.FeignClientConfig;
//import com.example.order_service.dto.ProductDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "PRODUCT-SERVICE") // port của Product Service
//public interface ProductClient {
//
//
//
//    @GetMapping("/api/products/{id}")
//    ProductDTO getProductById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
//}
//
