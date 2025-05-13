package com.example.delivery_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private Long id;
    private String trackingNumber;



    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private LocalDate deliveryDate;
    private String originAddress;
    private String destinationAddress;
    private Double shippingCost;
    private String deliveryMethod;

    private Long userId; // Người đặt hàng
    private Long assignedShipperId; // Nhân viên được giao

    @CreationTimestamp
    private LocalDateTime createdAt;


    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String recipientName; // tên người nhận

    private String recipientPhone; // số điện thoại người nhận


    private Long orderId;

    private boolean confirmed = false;
}
