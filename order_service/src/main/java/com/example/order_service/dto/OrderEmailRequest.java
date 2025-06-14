package com.example.order_service.dto;
import com.example.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEmailRequest {
    private String email;
    private String customerName;
    private Long orderId;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private Double totalAmount;
    private String paymentMethod;
    private String address;
    private String date;
}
