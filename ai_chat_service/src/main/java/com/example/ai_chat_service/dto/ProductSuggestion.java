package com.example.ai_chat_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSuggestion {
    private String name;
    private int price;
    private String image;
}
