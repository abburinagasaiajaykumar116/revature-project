package org.example.revshop.service.impl;

import org.example.revshop.JwtProvider;
import org.example.revshop.dtos.AuthResponse;
import org.example.revshop.dtos.LoginRequest;
import org.example.revshop.model.User;
import org.example.revshop.service.AuthService;
import org.example.revshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userService.login(request.getEmail(), request.getPassword());

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(
                token,
                user.getRole(),
                user.getUserId()   // 🔥 ADD THIS
        );
    }
}
