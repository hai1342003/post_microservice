package com.example.user_service.service.impl;

import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleEnum;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    @Cacheable(value = "userCache", key = "#userId")
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with given id: " + userId));

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User is not exists with given id" + userId)
        );

        user.setUsername(updatedUser.getName());

        if (updatedUser.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());

            user.setPassword(encodedPassword);
        }

        User updatedUserObj = userRepository.save(user);

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User is not exists with given id" + userId)
        );

        userRepository.deleteById(userId);
    }

    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    @Override
    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());

        return userRepository.save(user);
    }

}