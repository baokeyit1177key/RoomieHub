package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.response.UserPackageInfoResponse;
import org.example.roomiehub.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return ResponseEntity.ok(userDetails.getUsername());
        } else {
            return ResponseEntity.ok(principal.toString());
        }
    }

    @GetMapping("/{userId}/package")
    public ResponseEntity<UserPackageInfoResponse> getUserPackageInfo(@PathVariable Long userId) {
        UserPackageInfoResponse response = userService.getCurrentUserPackageInfo(userId);
        return ResponseEntity.ok(response);
    }
}
