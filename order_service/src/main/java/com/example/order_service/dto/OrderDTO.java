package com.example.order_service.dto;

import com.example.order_service.entity.Address;
import com.example.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;





@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long userId;
    private List<OrderItemDTO> items;
    private Double amount;
    private Address address;
    private String paymentMethod;

    // getter/setter
}