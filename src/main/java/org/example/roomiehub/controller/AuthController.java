package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.LoginRequest;
import org.example.roomiehub.dto.request.RegisterRequest;
import org.example.roomiehub.dto.response.AuthResponse;
import org.example.roomiehub.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/api/oauth2/google/url")
    public String getGoogleOAuth2Url() {
        // Đây là URL kích hoạt flow OAuth2 Google
        return "/oauth2/authorization/google";
    }
}

