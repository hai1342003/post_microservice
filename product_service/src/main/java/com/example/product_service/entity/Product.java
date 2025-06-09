package com.example.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) // Bắt buộc, giới hạn độ dài
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 50)
    private String category; // Danh mục sản phẩm

    // Lưu chuỗi JSON dạng ["8GB", "16GB"] hoặc "8GB,16GB"
    @Column(length = 100)
    private String ram;

    @Column(nullable = false)
    private Boolean bestseller;


    // Lưu tên file ảnh hoặc URL
    private String image1;
}
