package com.example.cart_service.controller;

import com.example.cart_service.dto.PaymentDTO;
import com.example.cart_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO processedPayment = paymentService.processPayment(paymentDTO);
        return ResponseEntity.ok(processedPayment);
    }


}
