package com.example.order_service.service.impl;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.repository.OrderItemRepository;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Order datDonHangCOD(OrderDTO request) {
        Order donHang = new Order();
        donHang.setUserId(request.getUserId());
        donHang.setAmount(request.getAmount());
        donHang.setAddress(request.getAddress());
        donHang.setPaymentMethod("COD");
        donHang.setPayment(false);
        donHang.setDate(LocalDateTime.now());
        donHang.setStatus("Order Placed");


        List<OrderItem> danhSachItem = request.getItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setOrder(donHang); // Liên kết ngược lại với Order
            return item;
        }).collect(Collectors.toList());



        donHang.setItems(danhSachItem);

        return orderRepository.save(donHang); // Cascade.ALL sẽ tự lưu OrderItem
    }

    @Override
    public Order datDonHangStripe(OrderDTO request) {
        Order donHang = datDonHangCOD(request);
        donHang.setPaymentMethod("Stripe");
        return orderRepository.save(donHang);
    }

    @Override
    public Order datDonHangRazorpay(OrderDTO request) {
        Order donHang = datDonHangCOD(request);
        donHang.setPaymentMethod("Razorpay");
        return orderRepository.save(donHang);
    }

    @Override
    public List<Order> layTatCaDonHang() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> layDonHangCuaUser(Long userId) {
        return List.of();
    }

    @Override
    public Order capNhatTrangThai(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
