//package com.example.user_service.controller;
//
//import com.example.user_service.dto.RegisterUserDto;
//import com.example.user_service.dto.UserDto;
//import com.example.user_service.entity.User;
//import com.example.user_service.security.AuthenticationService;
//import com.example.user_service.security.JwtService;
//import com.example.user_service.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    public void testCreateUser_AsAdmin() throws Exception {
//        UserDto input = new UserDto();
//        input.setName("admin");
//
//        UserDto saved = new UserDto();
//        saved.setId(1L);
//
//
//        saved.setName("admin");
//
//        when(userService.createUser(any())).thenReturn(saved);
//
//        mockMvc.perform(post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(input))
//                        .with(user("admin").roles("ADMIN")))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1L));
//    }
//
//
//    @Test
//    public void testAuthenticatedUser_ReturnsInfo() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setName("admin");
//
//        when(userService.getUserByUsername("admin")).thenReturn(userDto);
//
//        mockMvc.perform(get("/api/users/me").with(user("admin").roles("USER")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("admin"));
//    }
//
//    @Test
//    public void testAllUsers_AsAdmin() throws Exception {
//        User user1 = new User();
//        user1.setUsername("user1");
//        User user2 = new User();
//        user2.setUsername("user2");
//
//        when(userService.allUsers()).thenReturn(List.of(user1, user2));
//
//        mockMvc.perform(get("/api/users").with(user("admin").roles("ADMIN")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].username").value("user1"));
//    }
//
//    @Test
//    public void testGetUserById_AsAdmin() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setId(1L);
//        userDto.setName("admin");
//
//        when(userService.getUserById(1L)).thenReturn(userDto);
//
//        mockMvc.perform(get("/api/users/1").with(user("admin").roles("ADMIN")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("admin"));
//    }
//
//    @Test
//    public void testDeleteUser_AsSuperAdmin() throws Exception {
//        mockMvc.perform(delete("/api/users/1").with(user("superadmin").roles("SUPER_ADMIN")))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("User deleted successfully!")));
//    }
//}
