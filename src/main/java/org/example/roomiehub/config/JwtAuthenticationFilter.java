package org.example.roomiehub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

   @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    String path = request.getRequestURI();
    String method = request.getMethod();

    System.out.println(">>> [JwtFilter] Request Method: " + method + ", URI: " + path);

    // Các API public, không yêu cầu token
    if ("GET".equalsIgnoreCase(method) &&
            (path.equals("/api/apartments") ||
         path.equals("/api/apartments/count") ||
         path.equals("/api/apartments/create") ||
         path.equals("/api/payment/receive-hook") ||
         path.equals("/api/roommate-posts")   // Thêm dòng này
        )) {
        System.out.println(">>> [JwtFilter] Public GET path - skipping auth: " + path);
        filterChain.doFilter(request, response);
        return;
    }

    if ("OPTIONS".equalsIgnoreCase(method) ||
            path.startsWith("/api/auth/") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/swagger-resources") ||
            path.startsWith("/webjars") ||
            path.equals("/swagger-ui.html") ||
            path.equals("/") ||
            path.startsWith("/api/test-chatgpt") ||
          path.equals("/api/payment/receive-hook")

    ) {

        System.out.println(">>> [JwtFilter] Whitelisted path - skipping auth: " + path);
        filterChain.doFilter(request, response);
        return;
    }

    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        System.out.println(">>> [JwtFilter] Missing or invalid Authorization header");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
        return;
    }

    String token = authHeader.substring(7);
    String email;

    try {
        email = jwtUtil.extractEmail(token);
    } catch (Exception e) {
        System.out.println(">>> [JwtFilter] Failed to extract email from JWT: " + e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        return;
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (jwtUtil.validateToken(token, userDetails)) {
            System.out.println(">>> [JwtFilter] JWT token validated, setting authentication for user: " + email);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            System.out.println(">>> [JwtFilter] JWT token validation failed for user: " + email);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token validation failed");
            return;
        }
    }

    filterChain.doFilter(request, response);
}




    @Configuration
    public static class OpenApiConfig {
        @Bean
        public OpenAPI customOpenAPI() {
            final String securitySchemeName = "bearerAuth";

            return new OpenAPI()
                    .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList(securitySchemeName))
                    .components(new io.swagger.v3.oas.models.Components()
                            .addSecuritySchemes(securitySchemeName,
                                    new io.swagger.v3.oas.models.security.SecurityScheme()
                                            .name(securitySchemeName)
                                            .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                            ))
                    .servers(List.of(
                            new Server().url("http://localhost:8080"),
                            new Server().url("http://localhost:5173")
                    ));
        }
    }

    @Configuration
    public static class CorsConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                            .allowedOriginPatterns("http://localhost:8080", "https://roomiehub-production.up.railway.app" , "http://localhost:5173")

                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            };
        }
    }
}
