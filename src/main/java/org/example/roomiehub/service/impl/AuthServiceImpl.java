package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.dto.request.LoginRequest;
import org.example.roomiehub.dto.request.RegisterRequest;
import org.example.roomiehub.dto.response.LoginResponse;
import org.example.roomiehub.dto.response.RegisterResponse;
import org.example.roomiehub.exception.AuthException;
import org.example.roomiehub.model.User;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.AuthService;
import org.example.roomiehub.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            // Dùng lỗi 409 - Conflict (xử lý ở GlobalExceptionHandler)
            throw new AuthException("Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(List.of(Enums.Role.USER))
                .build();

        userRepository.save(user);

        return new RegisterResponse(user.getEmail(),
                                    user.getFullName(),
                                    user.getCreatedDate());
    }


    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new AuthException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("User not found"));

        //Lấy role đầu tiên (ví dụ chỉ có 1 vai trò duy nhất)
        String role = user.getRole().get(0).name();

        //Gọi hàm generateToken mới
        String token = jwtUtil.generateToken(user.getId().toString(), user.getEmail(), role);

        return new LoginResponse(token);
    }

}
