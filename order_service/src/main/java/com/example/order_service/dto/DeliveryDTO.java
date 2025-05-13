package com.example.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    private Long id;
    private String trackingNumber;
    private String status;
    private LocalDate deliveryDate;
    private String originAddress;
    private String destinationAddress;
    private Double shippingCost;
    private String deliveryMethod;

    private Long userId; // Người đặt hàng (để lọc theo user hoặc hiển thị)
    private Long assignedShipperId; // Nhân viên được phân công giao

    private LocalDateTime createdAt;



    private LocalDateTime updatedAt;
    private String recipientName;

    private String recipientPhone;
    private Long orderId;
}
