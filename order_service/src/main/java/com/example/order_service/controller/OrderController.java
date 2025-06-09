package com.example.order_service.controller;

import com.example.order_service.dto.AddressDTO;
import com.example.order_service.dto.OrderDTO;
import com.example.order_service.dto.OrderItemDTO;
import com.example.order_service.dto.UserDTO;
import com.example.order_service.entity.Address;
import com.example.order_service.entity.Order;
import com.example.order_service.service.OrderService;
import com.example.order_service.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;
    private final UserClient userClient;




    @PostMapping("/cod")
    public ResponseEntity<?> datDonHangCOD(@RequestBody OrderDTO request) {
        try {
            Order savedOrder = orderService.datDonHangCOD(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đặt hàng COD thành công!");
            response.put("orderId", savedOrder.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }




    @PostMapping("/stripe")
    public ResponseEntity<?> datDonHangStripe(@RequestBody OrderDTO request) {
        try {
            String sessionUrl = orderService.datDonHangStripe(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tạo phiên thanh toán Stripe thành công!");
            response.put("sessionUrl", sessionUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra khi tạo thanh toán Stripe: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @PostMapping("/verifyStripe")
    public ResponseEntity<?> xacNhanThanhToanStripe(@RequestBody Map<String, Object> request) {
        try {
            Boolean success = Boolean.valueOf(String.valueOf(request.get("success")));
            Long orderId = Long.valueOf(String.valueOf(request.get("orderId")));

            if (success != null && success) {
                orderService.xacNhanThanhToanStripe(orderId);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Xác nhận thanh toán Stripe thành công!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Thanh toán thất bại hoặc bị huỷ!");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 👉 thêm dòng này để in lỗi chi tiết trong console
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi xác nhận thanh toán: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }





    @PostMapping("/userorders")
    public ResponseEntity<?> layDonHangTheoUserDangNhap() {
        UserDTO user = userClient.layThongTinNguoiDungDangNhap();
        List<Order> orders = orderService.layDonHangCuaUser(user.getId());

        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> new OrderItemDTO(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getRam(),
                    item.getName(),
                    item.getPrice(),
                    item.getImage1()
            )).toList();

            Address address = order.getAddress();
            AddressDTO addressDTO = new AddressDTO(
                    address.getFirstName(),
                    address.getLastName(),
                    address.getEmail(),
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getZipCode(),
                    address.getCountry(),
                    address.getPhone()
            );

            return new OrderDTO(
                    order.getId(),
                    order.getUserId(),
                    order.getAmount(),
                    addressDTO,
                    itemDTOs,
                    order.getStatus().name(),
                    order.getPaymentMethod().name(),
                    order.getPayment(),
                    order.getDate()
            );
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orders", orderDTOs);
        return ResponseEntity.ok(response);
    }




    @PostMapping("/list")
    public ResponseEntity<?> layTatCaDonHang() {
        List<Order> orders = orderService.layTatCaDonHang();

        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            List<OrderItemDTO> itemDTOs = order.getItems().stream().map(item -> new OrderItemDTO(
                    item.getProductId(),
                    item.getQuantity(),
                    item.getRam(),
                    item.getName(),
                    item.getPrice(),
                    item.getImage1()
            )).toList();

            Address address = order.getAddress();
            AddressDTO addressDTO = new AddressDTO(
                    address.getFirstName(),
                    address.getLastName(),
                    address.getEmail(),
                    address.getStreet(),
                    address.getCity(),
                    address.getState(),
                    address.getZipCode(),
                    address.getCountry(),
                    address.getPhone()
            );

            return new OrderDTO(
                    order.getId(),
                    order.getUserId(),
                    order.getAmount(),
                    addressDTO,
                    itemDTOs,
                    order.getStatus().name(),
                    order.getPaymentMethod().name(),
                    order.getPayment(),
                    order.getDate()
            );
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orders", orderDTOs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status")
    public ResponseEntity<?> capNhatTrangThaiDonHang(@RequestBody Map<String, String> request) {
        try {
            Long orderId = Long.parseLong(request.get("orderId"));
            String status = request.get("status");

            orderService.capNhatTrangThai(orderId, status);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật trạng thái đơn hàng thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }









    @GetMapping("/stats/overview")
    public ResponseEntity<?> thongKeTongQuan() {
        try {
            List<Order> orders = orderService.layTatCaDonHang();

            int totalOrders = orders.size();
            double totalRevenue = orders.stream()
                    .filter(Order::getPayment)
                    .mapToDouble(Order::getAmount)
                    .sum();

            Map<String, Long> statusCounts = orders.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            order -> order.getStatus().name(),
                            java.util.stream.Collectors.counting()
                    ));

            Map<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("totalOrders", totalOrders);
            response.put("totalRevenue", totalRevenue);
            response.put("statusCounts", statusCounts);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi lấy thống kê tổng quan: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}



