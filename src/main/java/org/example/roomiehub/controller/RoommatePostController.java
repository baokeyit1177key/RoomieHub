package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.RoommatePostFilterRequest;
import org.example.roomiehub.dto.request.RoommatePostRequest;
import org.example.roomiehub.dto.response.RoommatePostResponse;
import org.example.roomiehub.service.RoommatePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roommate-posts")
@RequiredArgsConstructor
public class RoommatePostController {

    private final RoommatePostService roommatePostService;

    /**
     * Tạo bài đăng mới cho người dùng hiện tại.
     */
    @PostMapping
    public ResponseEntity<RoommatePostResponse> createRoommatePost(
            @RequestBody RoommatePostRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        RoommatePostResponse response = roommatePostService.createRoommatePost(request, userEmail);
        return ResponseEntity.ok(response);
    }

    /**
     * Cập nhật bài đăng nếu thuộc về người dùng hiện tại.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoommatePostResponse> updateRoommatePost(
            @PathVariable Long id,
            @RequestBody RoommatePostRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        RoommatePostResponse response = roommatePostService.updateRoommatePost(id, request, userEmail);
        return ResponseEntity.ok(response);
    }

    /**
     * Xóa bài đăng nếu thuộc về người dùng hiện tại.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoommatePost(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        roommatePostService.deleteRoommatePost(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lấy tất cả bài đăng công khai.
     */
    @GetMapping
    public ResponseEntity<List<RoommatePostResponse>> getAllPosts() {
        return ResponseEntity.ok(roommatePostService.getAllPosts());
    }

    /**
     * Lấy tất cả bài đăng của người dùng hiện tại.
     */
    @GetMapping("/my-posts")
    public ResponseEntity<List<RoommatePostResponse>> getUserRoommatePosts(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(roommatePostService.getUserRoommatePosts(userEmail));
    }

    /**
     * Lấy bài đăng theo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoommatePostResponse> getRoommatePostById(@PathVariable Long id) {
        return ResponseEntity.ok(roommatePostService.getRoommatePostById(id));
    }

    /**
     * Lọc bài đăng theo tiêu chí tùy chọn (DTO `RoommatePostFilterRequest`)
     */
  @PostMapping("/filter")
public ResponseEntity<List<RoommatePostResponse>> filterRoommatePosts(
        @RequestBody RoommatePostFilterRequest filterRequest) {
    return ResponseEntity.ok(roommatePostService.filterRoommatePosts(filterRequest));
}

}
