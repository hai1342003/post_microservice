//package com.example.user_service.controller;
//
//import com.example.user_service.dto.LoginUserDto;
//import com.example.user_service.dto.RegisterUserDto;
//import com.example.user_service.entity.User;
//import com.example.user_service.security.AuthenticationService;
//import com.example.user_service.security.JwtService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AuthenticationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @Mock
//    private UserDetailsService userDetailsService;
//
//
//    @MockBean
//    private AuthenticationService authenticationService;
//
//    @BeforeEach
//    void setup() {
//        // Mock hành vi trả về user khi loadUserByUsername được gọi
//        UserDetails mockUser = new org.springframework.security.core.userdetails.User(
//                "testuser", "password", List.of(() -> "USER")
//        );
//
//        when(jwtService.validateToken("valid-token")).thenReturn(true);
//        when(jwtService.extractUsername("valid-token")).thenReturn("testuser");
//        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(mockUser);
//    }
//
//    @Test
//    public void testRegister_Success() throws Exception {
//        RegisterUserDto input = new RegisterUserDto();
//        input.setUsername("testuser");
//        input.setPassword("testpass");
//
//        User mockUser = new User();
//        mockUser.setUsername("testuser");
//
//        when(authenticationService.signup(any())).thenReturn(mockUser);
//        when(jwtService.generateToken(any())).thenReturn("fake-token");
//
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(input)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("fake-token"));
//    }
//
//
//    @Test
//    public void testLogin_Success() throws Exception {
//        LoginUserDto loginDto = new LoginUserDto("testuser", "testpass");
//
//        User user = new User();
//        user.setUsername("testuser");
//
//        when(authenticationService.authenticate(any())).thenReturn(user);
//        when(jwtService.generateToken(any())).thenReturn("token123");
//        when(jwtService.getExpirationTime()).thenReturn(123456789L);
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(loginDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("token123"))
//                .andExpect(jsonPath("$.expiresAt").value(123456789L));
//    }
//
//    @Test
//    public void testValidateToken_ValidToken() throws Exception {
//        when(jwtService.validateToken("valid-token")).thenReturn(true);
//        when(jwtService.extractUsername("valid-token")).thenReturn("testuser");
//
//        mockMvc.perform(get("/auth/validate")
//                        .header("Authorization", "Bearer valid-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Valid Token"))
//                .andExpect(jsonPath("$.username").value("testuser"));
//    }
//}
