package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.response.RoommatePostResponse;
import org.example.roomiehub.service.RoommatePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RoommateRecommendationController {

    private final RoommatePostService roommatePostService;

    /**
     * Lấy danh sách căn hộ phù hợp với survey của user hiện tại (dựa trên email của user đăng nhập).
     */
    @GetMapping("/roommate-posts")
    public ResponseEntity<List<RoommatePostResponse>> getRecommendedRoommatePosts(Authentication authentication) {
        String userEmail = authentication.getName();
        List<RoommatePostResponse> recommendedPosts = roommatePostService.recommendRoommatePosts(userEmail);
        return ResponseEntity.ok(recommendedPosts);
    }
}
