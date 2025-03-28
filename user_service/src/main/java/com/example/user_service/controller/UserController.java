package com.example.user_service.controller;

import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.createUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or #userId == principal.id")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN') or #userId == principal.id")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId,
                                              @RequestBody UserDto updatedUser) {
        UserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully!");
    }
}