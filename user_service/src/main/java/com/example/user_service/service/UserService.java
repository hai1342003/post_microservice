package com.example.user_service.service;

import com.example.user_service.dto.CartData;
import com.example.user_service.dto.CartItemDto;
import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    UserDto getUserByUsername(String username);

    UserDto updateUser(Long userId, UserDto updatedUser);



    void deleteUser(Long userId);



    List<User> allUsers();

    User createAdministrator(RegisterUserDto registerUserDto);


    void updateCartData(String token, CartItemDto dto);

    void setCartItemQuantity(String token, CartItemDto dto);

    List<Map<String, Object>> getCartData(String token);

    List<UserDto> getAllShippers();
}

