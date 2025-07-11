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
        if ("GET".equalsIgnoreCase(request.getMethod()) && path.equals("/api/apartments")) {
    filterChain.doFilter(request, response);
    return;
}
         if ("OPTIONS".equalsIgnoreCase(request.getMethod()) ||
            path.startsWith("/api/auth/") ||
            path.startsWith("/v3/api-docs") ||
            path.startsWith("/swagger-ui") ||
            path.startsWith("/swagger-resources") ||
            path.startsWith("/webjars") ||
            path.equals("/swagger-ui.html") ||
            path.equals("/") ||
            path.startsWith("/api/test-chatgpt") ) {

            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
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
                            new Server().url("https://roomiehub-production.up.railway.app"),
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
