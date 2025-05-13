package com.example.user_service.controller;

import com.example.user_service.dto.CartData;
import com.example.user_service.dto.CartItemDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.User;
//import com.example.user_service.security.UserDetailsImpl;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        User currentUser = (User) authentication.getPrincipal();



//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        User currentUser = userDetails.getUser();

        String username = authentication.getName(); // username hiện tại
        UserDto userDto = userService.getUserByUsername(username); // viết hàm này trong service
        return ResponseEntity.ok(userDto);
//        return ResponseEntity.ok(currentUser);
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

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemDto dto, @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        userService.updateCartData(jwt, dto);
        return ResponseEntity.ok(Map.of("success", true, "message", "Item added to cart"));
    }

    @PostMapping("/cart/update")
    public ResponseEntity<?> updateCartQuantity(@RequestBody CartItemDto dto, @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        userService.setCartItemQuantity(jwt, dto);
        return ResponseEntity.ok(Map.of("success", true, "message", "Cập nhật số lượng thành công"));
    }


    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCartData(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Thiếu token"));
        }

        String jwt = authHeader.substring(7);
        List<Map<String, Object>> cartData = userService.getCartData(jwt);
        return ResponseEntity.ok(Map.of("success", true, "cartData", cartData));
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> fallbackAllUsers() {
        return allUsers();
    }


//    @GetMapping("/cart")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> getCartData() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        List<Map<String, Object>> cartData = userService.getCartDataByUsername(username);
//        return ResponseEntity.ok(Map.of("success", true, "cartData", cartData));
//    }


    @GetMapping("/shippers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllShippers() {
        List<UserDto> shippers = userService.getAllShippers(); // đã return DTO trong service
        return ResponseEntity.ok(shippers);
    }


}

