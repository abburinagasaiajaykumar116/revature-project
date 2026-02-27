package org.example.revshop.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.revshop.controllers.AuthController;
import org.example.revshop.dtos.AuthResponse;
import org.example.revshop.dtos.LoginRequest;
import org.example.revshop.model.User;
import org.example.revshop.security.JwtFilter;
import org.example.revshop.service.AuthService;
import org.example.revshop.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtFilter jwtFilter;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;



    @Test
    void testRegisterUser() throws Exception {

        User user = new User();
        user.setName("Bharath");
        user.setEmail("bharath@gmail.com");
        user.setPassword("1234");

        Mockito.doNothing().when(userService).register(Mockito.any(User.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void testLogin() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("bharath@gmail.com");
        request.setPassword("1234");

        AuthResponse response = new AuthResponse();
        response.setToken("dummy-jwt-token");
        response.setRole("USER");

        Mockito.when(authService.login(Mockito.any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}