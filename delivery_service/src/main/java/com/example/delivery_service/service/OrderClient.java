package com.example.delivery_service.service;

import com.example.delivery_service.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

    @GetMapping("/api/orders")
    List<OrderDTO> getAllOrders();

    @GetMapping("/api/orders/{orderId}")
    OrderDTO getOrderById(@PathVariable Long orderId);

    // Khi giao hàng xong thì gọi API này để cập nhật trạng thái đơn hàng thành "DELIVERED" hoặc "FAILED"
    @PutMapping("/api/orders/{orderId}/status")
    OrderDTO updateOrderStatus(@PathVariable Long orderId, @RequestParam String status);

    // Khi shipper muốn lấy danh sách đơn hàng cần giao hôm nay
    @GetMapping("/api/orders/pending-deliveries")
    List<OrderDTO> getPendingOrders();

}
