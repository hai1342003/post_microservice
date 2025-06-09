package com.example.ai_chat_service.controller;


import com.example.ai_chat_service.dto.ChatRequest;
import com.example.ai_chat_service.dto.ProductSuggestion;
import com.example.ai_chat_service.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/advice")
    public ResponseEntity<List<ProductSuggestion>> getAdvice(@RequestBody ChatRequest request) {
        List<ProductSuggestion> suggestions = chatService.getProductSuggestions(request.getMessage());
        return ResponseEntity.ok(suggestions);
    }
}
