package com.example.order_service.service;

import com.example.order_service.dto.OrderDTO;

public interface StripeService {
    String taoSessionStripe(OrderDTO orderDTO);
}
