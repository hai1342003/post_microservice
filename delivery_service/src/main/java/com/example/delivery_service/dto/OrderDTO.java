package com.example.delivery_service.dto;

import com.example.order_service.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long customerId;
    private List<OrderItemDTO> items;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String status;
}

