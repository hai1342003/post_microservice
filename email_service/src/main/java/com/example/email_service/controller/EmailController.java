package com.example.email_service.controller;


import com.example.email_service.dto.OrderEmailRequest;
import com.example.email_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-order-email")
    public ResponseEntity<String> sendOrderEmail(@RequestBody OrderEmailRequest request) {
        emailService.sendOrderConfirmationEmail(request);
        return ResponseEntity.ok("Email sent successfully");
    }
}
