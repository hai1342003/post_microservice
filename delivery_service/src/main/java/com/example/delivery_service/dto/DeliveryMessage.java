package com.example.delivery_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMessage {
    private Long orderId;
    private String trackingNumber;
    private String status;
    private String originAddress;
    private String destinationAddress;
    private Double shippingCost;
    private String deliveryMethod;
    private LocalDate deliveryDate;
}
