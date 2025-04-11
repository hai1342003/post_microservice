package com.example.delivery_service.controller;

import com.example.delivery_service.dto.OrderDTO;
import com.example.delivery_service.entity.Delivery;
import com.example.delivery_service.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(deliveryService.getAllOrders());
    }

    @Operation(summary = "Tạo đơn hàng mới", description = "Tạo một đơn hàng mới")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo đơn hàng thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        return new ResponseEntity<>(deliveryService.createDelivery(delivery), HttpStatus.CREATED);
    }

    @Operation(summary = "Lấy thông tin đơn hàng", description = "Lấy thông tin tất cả các đơn hàng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy đơn hàng")
    })
    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("Received JWT: " + authHeader);
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

    // Lấy chi tiết đơn hàng từ Order Service
    @GetMapping("/{orderId}/details")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(deliveryService.getOrderDetails(orderId));
    }

    // Cập nhật trạng thái đơn hàng khi giao xong
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        deliveryService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok().build();
    }

    // Lấy danh sách đơn hàng cần giao
    @GetMapping("/pending")
    public ResponseEntity<List<OrderDTO>> getPendingOrders() {
        return ResponseEntity.ok(deliveryService.getPendingOrders());
    }
}
