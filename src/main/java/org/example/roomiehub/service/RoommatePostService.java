package org.example.roomiehub.service;

import org.example.roomiehub.dto.request.RoommatePostFilterRequest;
import org.example.roomiehub.dto.request.RoommatePostRequest;
import org.example.roomiehub.dto.response.RoommatePostResponse;

import java.util.List;

public interface RoommatePostService {

    /**
     * Tạo mới một bài đăng tìm bạn cùng phòng, gán email từ người dùng đăng nhập.
     */
    RoommatePostResponse createRoommatePost(RoommatePostRequest request, String userEmail);

    /**
     * Lấy bài đăng theo ID.
     */
    RoommatePostResponse getRoommatePostById(Long id);

    /**
     * Lấy tất cả bài đăng (dành cho admin hoặc hiển thị public).
     */
    List<RoommatePostResponse> getAllPosts(); // <- Đổi từ getAllRoommatePosts() cho đồng bộ với Impl

    /**
     * Lấy tất cả bài đăng của người dùng đã đăng nhập.
     */
    List<RoommatePostResponse> getUserRoommatePosts(String userEmail);

    /**
     * Cập nhật bài đăng nếu người dùng là chủ bài đăng.
     */
    RoommatePostResponse updateRoommatePost(Long id, RoommatePostRequest request, String userEmail);

    /**
     * Xóa bài đăng nếu người dùng là chủ bài đăng.
     */
    void deleteRoommatePost(Long id, String userEmail);

    /**
     * Lọc bài đăng theo các tiêu chí phức tạp (địa chỉ, diện tích, giới tính, sở thích,...).
     */
    List<RoommatePostResponse> filterRoommatePosts(RoommatePostFilterRequest filterRequest);

      List<RoommatePostResponse> recommendRoommatePosts(String userEmail);

}
