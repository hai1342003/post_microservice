package com.example.user_service.bootstrap;

import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.RoleEnum;
import com.example.user_service.entity.User;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ShipperSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ShipperSeeder(RoleRepository roleRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createShippers();
    }

    private void createShippers() {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SHIPPER);
        if (optionalRole.isEmpty()) return;

        String[][] shippers = {
                {"Shipper One", "shipper1@email.com"},
                {"Shipper Two", "shipper2@email.com"}
        };


        for (String[] s : shippers) {
            RegisterUserDto userDto = new RegisterUserDto();
            userDto.setUsername(s[0]);
            userDto.setEmail(s[1]);
            userDto.setPassword("123456");

            Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
            if (optionalUser.isPresent()) continue;

            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRole(optionalRole.get());

            userRepository.save(user);
        }
    }
}
