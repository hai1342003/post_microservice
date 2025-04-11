package com.example.order_service.controller;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Đặt đơn hàng bằng COD
    @PostMapping("/cod")
    public Order placeOrderCod(@RequestBody OrderDTO request) {
        return orderService.datDonHangCOD(request);
    }

    // Đặt đơn hàng qua Stripe
    @PostMapping("/stripe")
    public Order placeOrderStripe(@RequestBody OrderDTO request) {
        return orderService.datDonHangStripe(request);
    }

    // Đặt đơn hàng qua Razorpay
    @PostMapping("/razorpay")
    public Order placeOrderRazorpay(@RequestBody OrderDTO request) {
        return orderService.datDonHangRazorpay(request);
    }

    // Lấy tất cả đơn hàng (cho admin)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.layTatCaDonHang();
    }

    // Lấy đơn hàng của 1 user
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.layDonHangCuaUser(userId);
    }

    // Cập nhật trạng thái đơn hàng
    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.capNhatTrangThai(orderId, status);
    }
}