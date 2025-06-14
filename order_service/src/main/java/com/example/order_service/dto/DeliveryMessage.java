package com.example.order_service.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryMessage implements Serializable {
    private Long orderId;
    private String trackingNumber;
    private String status;
    private String originAddress;
    private String destinationAddress;
    private Double shippingCost;
    private String deliveryMethod;
    private LocalDate deliveryDate;
}
