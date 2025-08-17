package org.example.roomiehub.config;

import io.netty.handler.codec.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
                          JwtAuthEntryPoint jwtAuthEntryPoint,
                          CustomUserDetailsService customUserDetailsService, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // ✅ dùng CorsConfigurationSource bên ngoài
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/api/roommate-posts/**", "GET")).permitAll()

                        .requestMatchers(
                                "/api/apartments",
                                "/api/apartments/count",
                                "/api/apartment-recommendation",
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-resources",
                                "/configuration/ui",
                                "/configuration/security",
                                "/webjars/**",
                                "/api/test-chatgpt",
                                "/api/surveys",
                                "/api/payment/receive-hook",
                                "/api/roommate-posts"

                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(oAuth2SuccessHandler)
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
        "http://localhost:5173",
        "https://roomiehub.onrender.com",
                "https://fe-roomiehub-production.up.railway.app"
    ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 👈 Quan trọng: Cho phép credentials
        config.setMaxAge(3600L); // Cache preflight

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}