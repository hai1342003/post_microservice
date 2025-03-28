package com.example.delivery_service.controller;

import com.example.delivery_service.dto.DeliveryDTO;
import com.example.delivery_service.dto.OrderDTO;
import com.example.delivery_service.entity.Delivery;
import com.example.delivery_service.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;





@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        return new ResponseEntity<>(deliveryService.createDelivery(delivery), HttpStatus.CREATED);
    }

    @GetMapping("/order-status/{orderId}")
    public ResponseEntity<OrderDTO> getOrderStatus(@PathVariable Long orderId) {
        OrderDTO order = deliveryService.getOrderStatus(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        return new ResponseEntity<>(deliveryService.getAllDeliveries(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Long id) {
        return new ResponseEntity<>(deliveryService.getDeliveryById(id), HttpStatus.OK);
    }


    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Delivery> getDeliveryByTrackingNumber(@PathVariable String trackingNumber) {
        return new ResponseEntity<>(deliveryService.getDeliveryByTrackingNumber(trackingNumber), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(@PathVariable Long id, @RequestParam String status) {
        return new ResponseEntity<>(deliveryService.updateDeliveryStatus(id, status), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
