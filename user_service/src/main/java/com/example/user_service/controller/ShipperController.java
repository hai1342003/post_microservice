package com.example.user_service.controller;

import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/shippers")
@RestController
public class ShipperController {
    private final UserService userService;

    public ShipperController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<User> createShipper(@RequestBody RegisterUserDto registerUserDto) {
        User createdShipper = userService.createShipper(registerUserDto);
        return ResponseEntity.ok(createdShipper);
    }
}
