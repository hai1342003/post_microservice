package com.example.order_service.service.impl;

import com.example.order_service.dto.OrderDTO;
import com.example.order_service.service.StripeService;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {

    public StripeServiceImpl(@Value("${stripe.secret.key}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

    @Override
    public String taoSessionStripe(OrderDTO orderDTO) {
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Đơn hàng từ LaptopShop")
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount((long) (orderDTO.getAmount() * 100)) // USD -> cent
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem item =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();



        String successUrl = "http://localhost:5173/verify?success=true&orderId=" + orderDTO.getId();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addLineItem(item)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(successUrl)
                        .setCancelUrl("http://localhost:5173/cart")
                        .build();


        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Tạo session Stripe thất bại: " + e.getMessage());
        }
    }
}
