package com.example.user_service.service;

import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId);

    UserDto updateUser(Long userId, UserDto updatedUser);

    void deleteUser(Long userId);

    List<User> allUsers();

    User createAdministrator(RegisterUserDto registerUserDto);
}

