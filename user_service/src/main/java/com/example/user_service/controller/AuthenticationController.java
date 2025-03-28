package com.example.user_service.controller;

import com.example.user_service.dto.LoginUserDto;
import com.example.user_service.dto.RegisterUserDto;
import com.example.user_service.entity.User;
import com.example.user_service.response.LoginResponse;
import com.example.user_service.security.AuthenticationService;
import com.example.user_service.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }




    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        token = token.substring(7); // Bỏ "Bearer "

        if (!jwtService.validateToken(token)) { // Kiểm tra token hợp lệ không
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok("Valid Token for user: " + username);
    }


    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());


        return ResponseEntity.ok(loginResponse);
    }
}