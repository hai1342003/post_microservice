package com.example.order_service.service;

//import com.example.order_service.config.FeignClientConfig;
import com.example.order_service.config.FeignConfig;
import com.example.order_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE", configuration = FeignConfig.class)
public interface UserClient {
    @GetMapping("/api/users/me")
    UserDTO layThongTinNguoiDungDangNhap();
}

