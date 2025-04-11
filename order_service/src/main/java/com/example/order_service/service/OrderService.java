package com.example.order_service.service;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;

import java.util.List;

public interface OrderService {
    Order datDonHangCOD(OrderDTO request);
    Order datDonHangStripe(OrderDTO request);
    Order datDonHangRazorpay(OrderDTO request);
    List<Order> layTatCaDonHang();
    List<Order> layDonHangCuaUser(Long userId);
    Order capNhatTrangThai(Long orderId, String status);
}