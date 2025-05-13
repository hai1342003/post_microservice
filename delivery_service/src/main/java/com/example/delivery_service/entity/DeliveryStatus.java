package com.example.delivery_service.entity;

public enum DeliveryStatus {
    PENDING,        // Mới tạo, chưa có nhân viên nhận
    ASSIGNED,       // Đã có nhân viên nhận giao
    IN_TRANSIT,     // Đang giao
    DELIVERED,      // Giao thành công
    FAILED          // Giao thất bại
}
