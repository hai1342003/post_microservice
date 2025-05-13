//package com.example.user_service.controller;
//
//
//
//import com.example.user_service.dto.RegisterUserDto;
//import com.example.user_service.entity.Role;
//import com.example.user_service.entity.RoleEnum;
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
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AdminControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @Test
//    public void testCreateAdmin_Success() throws Exception {
//        RegisterUserDto dto = new RegisterUserDto();
//
//
//        dto.setUsername("newadmin");
//        User admin = new User();
//        admin.setUsername("newadmin");
//
//
//        Role role = new Role();
//        role.setName(RoleEnum.ADMIN);
//        admin.setRole(role);
//
//        when(userService.createAdministrator(any())).thenReturn(admin);
//
//        mockMvc.perform(post("/admins")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(dto))
//                        .with(user("superadmin").roles("SUPER_ADMIN")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("newadmin"));
//    }
//}
