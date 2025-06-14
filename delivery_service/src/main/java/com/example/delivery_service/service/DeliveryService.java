package com.example.delivery_service.service;

import com.example.delivery_service.entity.Delivery;
import com.example.delivery_service.entity.DeliveryStatus;
import com.example.delivery_service.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Service
public class DeliveryService {


    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;

    public DeliveryService(DeliveryRepository deliveryRepository, OrderClient orderClient) {
        this.deliveryRepository = deliveryRepository;
        this.orderClient = orderClient;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public Delivery updateDelivery(Long id, Delivery delivery) {
        Delivery existing = getDeliveryById(id);
        if (existing == null) return null;


        existing.setAssignedShipperId(delivery.getAssignedShipperId());


        existing.setTrackingNumber(delivery.getTrackingNumber());
        existing.setStatus(DeliveryStatus.ASSIGNED);
        existing.setDeliveryDate(delivery.getDeliveryDate());
        existing.setOriginAddress(delivery.getOriginAddress());
        existing.setDestinationAddress(delivery.getDestinationAddress());
        existing.setShippingCost(delivery.getShippingCost());
        existing.setDeliveryMethod(delivery.getDeliveryMethod());
        existing.setOrderId(delivery.getOrderId());

        return deliveryRepository.save(existing);
    }

    public void deleteDelivery(Long id) {
        deliveryRepository.deleteById(id);
    }

    public Delivery updateDeliveryStatus(Long id, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setStatus(status);
        if (status == DeliveryStatus.DELIVERED) {
            delivery.setConfirmed(false); // để admin còn xác nhận
        }
        delivery.setUpdatedAt(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getDeliveriesByShipperId(Long shipperId) {
        return deliveryRepository.findByAssignedShipperId(shipperId);
    }



    public Delivery updateStatus(Long id, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        delivery.setStatus(status);
        return deliveryRepository.save(delivery);
    }






    public void confirmDelivery(Long id, boolean approved) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        if (delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != DeliveryStatus.FAILED) {
            throw new RuntimeException("Only DELIVERED or FAILED deliveries can be confirmed");
        }


        delivery.setConfirmed(approved);
        deliveryRepository.save(delivery);





        // Gọi Order Service để cập nhật trạng thái đơn hàng
        String newStatus = approved ? "DELIVERED" : "CANCELLED";
        Map<String, String> request = new HashMap<>();
        request.put("orderId", delivery.getOrderId().toString());
        request.put("status", newStatus);

        try {
            orderClient.updateOrderStatus(request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage());
        }
    }

}

