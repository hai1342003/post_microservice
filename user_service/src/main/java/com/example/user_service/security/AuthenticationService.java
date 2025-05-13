package com.example.user_service.security;


import com.example.user_service.dto.LoginUserDto;
import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleEnum;
import com.example.user_service.entity.User;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }




        if (userRepository.existsByUsername(input.getUsername())) {
            throw new RuntimeException("Username does not exist");
        }
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Email does not exist");
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());
        user.setRole(optionalRole.get());




        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid username or password");
        }

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }
}