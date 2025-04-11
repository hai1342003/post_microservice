package com.example.user_service.service.impl;

import com.example.user_service.dto.CartData;
import com.example.user_service.dto.CartItemDto;
import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.dto.UserDto;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleEnum;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtService;
import com.example.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    private final JwtService jwtService;

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


    public void updateCartData(String token, CartItemDto dto) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }


        if (dto.getItemId() == null || dto.getRam() == null) {
            throw new RuntimeException("Thiếu itemId hoặc ram");
        }

        ObjectMapper mapper = new ObjectMapper();
        CartData cartData;

        try {
            String existingCartJson = user.getCartData();

            if (existingCartJson != null && !existingCartJson.isEmpty()) {
                cartData = mapper.readValue(existingCartJson, CartData.class);
            } else {
                cartData = new CartData();
            }

            Map<String, Map<String, Integer>> items = cartData.getItems();

            String productIdStr = String.valueOf(dto.getItemId());
            String ram = dto.getRam();
            int quantity = dto.getQuantity();

            items.putIfAbsent(productIdStr, new HashMap<>());
            Map<String, Integer> ramMap = items.get(productIdStr);
            ramMap.put(ram, ramMap.getOrDefault(ram, 0) + quantity);

            String updatedJson = mapper.writeValueAsString(cartData);
            user.setCartData(updatedJson);
            userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật giỏ hàng: " + e.getMessage());
        }
    }



    public void setCartItemQuantity(String token, CartItemDto dto) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy user");
        }

        if (dto.getItemId() == null || dto.getRam() == null) {
            throw new RuntimeException("Thiếu itemId hoặc ram");
        }

        ObjectMapper mapper = new ObjectMapper();
        CartData cartData;

        try {
            String existingCartJson = user.getCartData();

            if (existingCartJson != null && !existingCartJson.isEmpty()) {
                cartData = mapper.readValue(existingCartJson, CartData.class);
            } else {
                cartData = new CartData();
            }

            Map<String, Map<String, Integer>> items = cartData.getItems();
            String productIdStr = String.valueOf(dto.getItemId());
            String ram = dto.getRam();
            int quantity = dto.getQuantity();

            if (quantity <= 0) {
                // Xoá nếu số lượng nhỏ hơn hoặc bằng 0
                if (items.containsKey(productIdStr)) {
                    items.get(productIdStr).remove(ram);
                    if (items.get(productIdStr).isEmpty()) {
                        items.remove(productIdStr);
                    }
                }
            } else {
                items.putIfAbsent(productIdStr, new HashMap<>());
                items.get(productIdStr).put(ram, quantity);
            }

            String updatedJson = mapper.writeValueAsString(cartData);
            user.setCartData(updatedJson);
            userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật số lượng: " + e.getMessage());
        }
    }



    public List<Map<String, Object>> getCartData(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng");
        }

        String cartJson = user.getCartData();
        if (cartJson == null || cartJson.isEmpty()) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu chưa có giỏ hàng
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            CartData cartData = mapper.readValue(cartJson, CartData.class);
            Map<String, Map<String, Integer>> items = cartData.getItems();

            List<Map<String, Object>> result = new ArrayList<>();

            for (Map.Entry<String, Map<String, Integer>> entry : items.entrySet()) {
                Long itemId = Long.valueOf(entry.getKey());
                Map<String, Integer> ramMap = entry.getValue();

                for (Map.Entry<String, Integer> ramEntry : ramMap.entrySet()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", itemId);
                    item.put("ram", ramEntry.getKey());
                    item.put("quantity", ramEntry.getValue());
                    result.add(item);
                }
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đọc dữ liệu giỏ hàng: " + e.getMessage());
        }
    }

}