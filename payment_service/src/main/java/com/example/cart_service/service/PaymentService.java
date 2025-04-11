package com.example.cart_service.service;

import com.example.cart_service.dto.PaymentDTO;
import com.example.cart_service.entity.Payment;
import com.example.cart_service.entity.PaymentStatus;
import com.example.cart_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentDTO processPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setOrderId(paymentDTO.getOrderId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        // Lưu payment vào database
        payment = paymentRepository.save(payment);

        // Cập nhật DTO trả về
        paymentDTO.setId(payment.getId());
        paymentDTO.setStatus(payment.getStatus().toString());
        paymentDTO.setPaymentDate(payment.getPaymentDate());

        return paymentDTO;
    }

}
