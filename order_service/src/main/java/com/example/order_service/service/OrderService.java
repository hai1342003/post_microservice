package com.example.order_service.service;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;

import java.util.List;

public interface OrderService {
    Order datDonHangCOD(OrderDTO request);
    String datDonHangStripe(OrderDTO request);
    Order luuTamThoiDonHang(OrderDTO request);
    Order datDonHangRazorpay(OrderDTO request);
    List<Order> layTatCaDonHang();

    void xacNhanThanhToanStripe(Long orderId);
    List<Order> layDonHangCuaUser(Long userId);
    void capNhatTrangThai(Long orderId, String status);
}