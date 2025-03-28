package com.example.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String name;
    private String type;
    private Double minPrice;
    private Double maxPrice;
    // Getters and Setters
}
