package com.example.delivery_service.controller;

import com.example.delivery_service.entity.Delivery;
import com.example.delivery_service.entity.DeliveryStatus;
import com.example.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;


    @GetMapping
    public ResponseEntity<?> layTatCaDonGiaoHang() {
        try {
            List<Delivery> deliveries = deliveryService.getAllDeliveries();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("deliveries", deliveries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi lấy danh sách đơn giao hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> layDonGiaoHangTheoId(@PathVariable Long id) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("delivery", delivery);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi lấy đơn giao hàng theo ID: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @GetMapping("/assigned/{shipperId}")
    public ResponseEntity<?> layDonGiaoTheoShipper(@PathVariable Long shipperId) {
        try {
            List<Delivery> deliveries = deliveryService.getDeliveriesByShipperId(shipperId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("deliveries", deliveries);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi lấy đơn theo shipper: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> taoDonGiaoHang(@RequestBody Delivery delivery) {
        try {
            Delivery saved = deliveryService.createDelivery(delivery);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tạo đơn giao hàng thành công!");
            response.put("delivery", saved);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi tạo đơn giao hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> capNhatDonGiaoHang(@PathVariable Long id, @RequestBody Delivery delivery) {
        try {
            Delivery updated = deliveryService.updateDelivery(id, delivery);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật đơn giao hàng thành công!");
            response.put("delivery", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật đơn giao hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> xoaDonGiaoHang(@PathVariable Long id) {
        try {
            deliveryService.deleteDelivery(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Xóa đơn giao hàng thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi xóa đơn giao hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> capNhatTrangThaiDon(@PathVariable Long id, @RequestParam("status") DeliveryStatus status) {
        try {
            Delivery updated = deliveryService.updateDeliveryStatus(id, status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật trạng thái đơn thành công!");
            response.put("delivery", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật trạng thái đơn: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }






    @PatchMapping("/{id}/confirm")
    public ResponseEntity<?> confirmDelivery(@PathVariable Long id, @RequestParam(required = false) Boolean approved) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (approved == null) {
                throw new IllegalArgumentException("approved parameter is required");
            }
            deliveryService.confirmDelivery(id, approved);
            response.put("success", true);
            response.put("message", "Delivery confirmation updated");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Xác nhận thất bại: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }


}



