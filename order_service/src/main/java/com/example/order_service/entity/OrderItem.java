package com.example.order_service.entity;

import com.example.order_service.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long productId;
    private Integer quantity;

    private String ram;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    private String name;
    private Double price;


    private String image1;
    // getter/setter
}