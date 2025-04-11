package com.example.delivery_service.service;

import com.example.delivery_service.dto.OrderDTO;
import com.example.delivery_service.entity.Delivery;
import com.example.delivery_service.repository.DeliveryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DeliveryService {


    @Autowired
    private DeliveryRepository deliveryRepository;


    @Autowired
    private final OrderClient orderClient;

    public DeliveryService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }


    public OrderDTO getOrderDetails(Long orderId) {
        return orderClient.getOrderById(orderId);
    }

    public OrderDTO updateOrderStatus(Long orderId, String status) {

        return orderClient.updateOrderStatus(orderId, status);
    }

    public List<OrderDTO> getAllOrders() {
        return orderClient.getAllOrders();
    }

    public List<OrderDTO> getPendingOrders() {
        return orderClient.getPendingOrders();
    }

    // Tạo mới một delivery
    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    // Lấy danh sách tất cả các deliveries
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    // Lấy delivery theo ID
    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
    }

    // Lấy delivery theo tracking number
    public Delivery getDeliveryByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
    }

    // Xóa delivery theo ID
    @Transactional
    public void deleteDelivery(Long id) {
        Delivery delivery = getDeliveryById(id);
        deliveryRepository.delete(delivery);
    }

    // Cập nhật trạng thái delivery
    @Transactional
    public Delivery updateDeliveryStatus(Long id, String status) {
        Delivery delivery = getDeliveryById(id);
        delivery.setStatus(status);
        return deliveryRepository.save(delivery);
    }

    // Cập nhật trạng thái bằng trackingNumber (dùng khi nhận message từ RabbitMQ)
    @Transactional
    public Delivery updateDeliveryStatusByTrackingNumber(String trackingNumber, String status) {
        Delivery delivery = getDeliveryByTrackingNumber(trackingNumber);
        delivery.setStatus(status);
        return deliveryRepository.save(delivery);
    }

    // RabbitListener để lắng nghe message từ RabbitMQ và xử lý cập nhật trạng thái delivery
//    @RabbitListener(queues = "order.queue")
//    public void receivedMessage(String message) {
//        System.out.println("Received message: " + message);
//
//        // Giả sử message chứa tracking number và trạng thái mới dưới dạng "trackingNumber:status"
//        String[] messageParts = message.split(":");
//        if (messageParts.length == 2) {
//            String trackingNumber = messageParts[0];
//            String newStatus = messageParts[1];
//
//            // Cập nhật trạng thái delivery dựa trên trackingNumber
//            updateDeliveryStatusByTrackingNumber(trackingNumber, newStatus);
//            System.out.println("Updated delivery with tracking number " + trackingNumber + " to status: " + newStatus);
//        } else {
//            System.out.println("Invalid message format. Expected 'trackingNumber:status'.");
//        }
//    }
}
