package com.example.order_service.service;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.entity.OrderStatus;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;


    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getDeliveryStatus(Long orderId) {
        String apiUrl = "http://localhost:8081/api/deliveries/" + orderId;
        return restTemplate.getForObject(apiUrl, String.class);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDTO(order);
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setCustomerId(orderDTO.getCustomerId());
        order.setItems(orderDTO.getItems().stream()
                .map(item -> new OrderItem(null, item.getProductId(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList()));
        order.setTotalPrice(orderDTO.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order = orderRepository.save(order);

        System.out.println("Order created: " + order.getId());

        String message = "New Order Create With ID: " + order.getId();
        System.out.println("Message sent: " + message);

        return convertToDTO(order);
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        order = orderRepository.save(order);
        return convertToDTO(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCustomerId(),
                order.getItems().stream()
                        .map(item -> new OrderItemDTO(item.getProductId(), item.getQuantity(), item.getPrice()))
                        .collect(Collectors.toList()),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getStatus().name()
        );
    }
}
