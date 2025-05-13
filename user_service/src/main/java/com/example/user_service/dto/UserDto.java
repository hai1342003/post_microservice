package com.example.user_service.dto;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String password;

    private Role role;

    public UserDto(Long id, String username) {
    }
}

