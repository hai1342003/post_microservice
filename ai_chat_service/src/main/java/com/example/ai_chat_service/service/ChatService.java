package com.example.ai_chat_service.service;


import com.example.ai_chat_service.dto.ProductSuggestion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    public List<ProductSuggestion> getProductSuggestions(String message) {
        String lower = message.toLowerCase();
        if (lower.contains("gaming") && lower.contains("20")) {
            return List.of(
                    new ProductSuggestion("MSI Gaming GF63", 18990000, "https://image-link.com/msi.jpg"),
                    new ProductSuggestion("ASUS TUF F15", 19900000, "https://image-link.com/asus.jpg")
            );
        } else {
            return List.of(
                    new ProductSuggestion("Dell Inspiron 15", 15990000, "https://image-link.com/dell.jpg"),
                    new ProductSuggestion("HP Pavilion", 14900000, "https://image-link.com/hp.jpg")
            );
        }
    }
}
