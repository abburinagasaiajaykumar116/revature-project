package org.example.revshop.service;


import org.example.revshop.dtos.AuthResponse;
import org.example.revshop.dtos.LoginRequest;



public interface AuthService {

    AuthResponse login(LoginRequest request);
}


