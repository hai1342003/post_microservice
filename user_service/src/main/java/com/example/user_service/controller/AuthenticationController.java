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

import java.util.Map;

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
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Missing or invalid Authorization header"));
        }

        token = token.substring(7);

        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Token"));
        }
        String username = jwtService.extractUsername(token);

        return ResponseEntity.ok(Map.of("message", "Valid Token", "username", username));
    }



    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        String jwtToken = jwtService.generateToken(registeredUser);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", jwtToken
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "token", jwtToken,
                "expiresAt", jwtService.getExpirationTime()
        ));
    }

}