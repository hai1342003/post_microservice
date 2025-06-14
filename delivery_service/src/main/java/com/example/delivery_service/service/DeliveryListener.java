package com.example.delivery_service.service;


import com.example.delivery_service.dto.DeliveryMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.delivery_service.config.RabbitMQConfig;


@Component
public class DeliveryListener {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleDeliveryMessage(DeliveryMessage message) {
        System.out.println("Received delivery message: " + message);
    }
}
