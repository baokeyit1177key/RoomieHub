package org.example.roomiehub.service;

import org.example.roomiehub.dto.request.LoginRequest;
import org.example.roomiehub.dto.request.RegisterRequest;
import org.example.roomiehub.dto.response.LoginResponse;
import org.example.roomiehub.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);

}
