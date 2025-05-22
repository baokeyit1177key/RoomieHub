package org.example.roomiehub.service;

import org.example.roomiehub.dto.request.LoginRequest;
import org.example.roomiehub.dto.request.RegisterRequest;
import org.example.roomiehub.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
