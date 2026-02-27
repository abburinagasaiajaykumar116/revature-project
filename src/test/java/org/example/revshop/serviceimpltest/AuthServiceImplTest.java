package org.example.revshop.serviceimpltest;

import org.example.revshop.JwtProvider;
import org.example.revshop.dtos.AuthResponse;
import org.example.revshop.dtos.LoginRequest;
import org.example.revshop.model.User;
import org.example.revshop.service.UserService;
import org.example.revshop.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testLogin_Success() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setRole("USER");
        mockUser.setUserId(1);

        when(userService.login("test@example.com", "password123"))
                .thenReturn(mockUser);

        when(jwtProvider.generateToken("test@example.com", "USER"))
                .thenReturn("mocked-jwt-token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
        assertEquals(Integer.valueOf(1), response.getUserId());

        verify(userService, times(1))
                .login("test@example.com", "password123");

        verify(jwtProvider, times(1))
                .generateToken("test@example.com", "USER");
    }

    @Test
    void testLogin_InvalidCredentials_ShouldThrowException() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrongpass");

        when(userService.login("wrong@example.com", "wrongpass"))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid credentials", exception.getMessage());

        verify(userService, times(1))
                .login("wrong@example.com", "wrongpass");

        verify(jwtProvider, never())
                .generateToken(anyString(), anyString());
    }
}