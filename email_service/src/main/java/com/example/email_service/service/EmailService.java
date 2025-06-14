package com.example.email_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.email_service.dto.OrderEmailRequest;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(OrderEmailRequest request) {
        StringBuilder body = new StringBuilder();

        body.append("Hello ").append(request.getCustomerName()).append(",\n\n");
        body.append("Your order ").append(request.getOrderId()).append(" has been ")
                .append(request.getStatus()).append(".\n\n");
        body.append("🧾 Order Details:\n");
        for (var item : request.getItems()) {
            body.append("- ").append(item.getName())
                    .append(" x").append(item.getQuantity()).append("\n");
        }
        body.append("\n💰 Total Amount: ").append(String.format("%,.0f", request.getTotalAmount())).append(" VND\n\n");
        body.append("Thank you for shopping with us!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("[Order Confirmation] " + request.getOrderId());
        message.setText(body.toString());

        mailSender.send(message);
    }
}

