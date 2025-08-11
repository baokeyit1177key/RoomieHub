package org.example.roomiehub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.model.User;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Google account missing email");
            return;
        }

        // Check or create user
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setProvider("GOOGLE"); // Hoặc "FACEBOOK" tuỳ hệ thống bạn`
            newUser.setRole(Enums.Role.USER); // hoặc ROLE_USER tuỳ hệ thống bạn
            return userRepository.save(newUser);
        });

        // Generate token
        String token = jwtUtil.generateToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : "USER"
        );

        // === Trả kết quả ===
        if (isAjaxRequest(request)) {
            // Trả JSON cho client-side app
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), new TokenResponse(token));
        } else {
            // Redirect về frontend kèm token
            response.sendRedirect("http://localhost:5173/oauth2/success?token=" + token);
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }

    // Inner DTO class
    record TokenResponse(String token) {}
}
