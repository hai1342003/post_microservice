package com.example.delivery_service.entity;

import com.example.order_service.entity.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    private String status;
    private LocalDate deliveryDate;
    private String originAddress;
    private String destinationAddress;
    private Double shippingCost;
    private String deliveryMethod;


    private Long OrderId;
}
