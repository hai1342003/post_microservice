package com.example.cart_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private String orderId;
    private Double amount;
    private String status;
    private LocalDateTime paymentDate;
}
