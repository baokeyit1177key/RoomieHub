//package org.example.roomiehub.service.impl;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.example.roomiehub.dto.request.RoommatePostRequest;
//import org.example.roomiehub.dto.response.RoommatePostResponse;
//import org.example.roomiehub.model.RoommatePost;
//import org.example.roomiehub.model.RoommatePreference;
//import org.example.roomiehub.repository.RoommatePostRepository;
//import org.example.roomiehub.repository.RoommatePreferenceRepository;
//import org.example.roomiehub.repository.UserRepository;
//import org.example.roomiehub.service.RoommatePostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class RoommatePostServiceImpl implements RoommatePostService {
//
//    @Autowired
//    private RoommatePostRepository roommatePostRepository;
//
//    @Autowired
//    private RoommatePreferenceRepository roommatePreferenceRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public RoommatePostResponse createRoommatePost(RoommatePostRequest request, String userEmail) {
//        // Kiểm tra user có tồn tại không
//        userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));
//
//        // Tạo RoommatePost
//        RoommatePost post = RoommatePost.builder()
//                .ownerPost(userEmail) // Gán email người dùng vào ownerPost
//                .address(request.getAddress())
//                .areaSquareMeters(request.getAreaSquareMeters())
//                .monthlyRentPrice(request.getMonthlyRentPrice())
//                .description(request.getDescription())
//                .build();
//
//        // Lưu RoommatePost
//        RoommatePost savedPost = roommatePostRepository.save(post);
//
//        // Tạo và lưu danh sách RoommatePreference
//        if (request.getRoommatePreferences() != null) {
//            request.getRoommatePreferences().forEach(prefRequest -> {
//                RoommatePreference preference = RoommatePreference.builder()
//                        .name(prefRequest.getName())
//                        .dateOfBirth(prefRequest.getDateOfBirth())
//                        .gender(prefRequest.getGender())
//                        .occupation(prefRequest.getOccupation())
//                        .description(prefRequest.getDescription())
//                        .preferredPersonality(prefRequest.getPreferredPersonality())
//                        .canCook(prefRequest.getCanCook())
//                        .isNightOwl(prefRequest.getIsNightOwl())
//                        .hasPet(prefRequest.getHasPet())
//                        .smokes(prefRequest.getSmokes())
//                        .oftenBringsFriendsOver(prefRequest.getOftenBringsFriendsOver())
//                        .roommatePost(savedPost)
//                        .build();
//                roommatePreferenceRepository.save(preference);
//            });
//        }
//
//        // Tạo response
//        return mapToResponse(savedPost);
//    }
//
//    @Override
//    public RoommatePostResponse getRoommatePostById(Long id) {
//        RoommatePost post = roommatePostRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("RoommatePost not found with id: " + id));
//        return mapToResponse(post);
//    }
//
//    @Override
//    public List<RoommatePostResponse> getAllRoommatePosts() {
//        return roommatePostRepository.findAll().stream()
//                .map(this::mapToResponse)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public RoommatePostResponse updateRoommatePost(Long id, RoommatePostRequest request, String userEmail) {
//        RoommatePost post = roommatePostRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("RoommatePost not found with id: " + id));
//
//        // Kiểm tra quyền sở hữu
//        if (!post.getOwnerPost().equals(userEmail)) {
//            throw new SecurityException("You are not authorized to update this post");
//        }
//
//        // Cập nhật thông tin RoommatePost
//        post.setAddress(request.getAddress());
//        post.setAreaSquareMeters(request.getAreaSquareMeters());
//        post.setMonthlyRentPrice(request.getMonthlyRentPrice());
//        post.setDescription(request.getDescription());
//
//        // Xóa các RoommatePreference cũ
//        roommatePreferenceRepository.deleteByRoommatePostId(id);
//
//        // Thêm các RoommatePreference mới
//        if (request.getRoommatePreferences() != null) {
//            request.getRoommatePreferences().forEach(prefRequest -> {
//                RoommatePreference preference = RoommatePreference.builder()
//                        .name(prefRequest.getName())
//                        .dateOfBirth(prefRequest.getDateOfBirth())
//                        .gender(prefRequest.getGender())
//                        .occupation(prefRequest.getOccupation())
//                        .description(prefRequest.getDescription())
//                        .preferredPersonality(prefRequest.getPreferredPersonality())
//                        .canCook(prefRequest.getCanCook())
//                        .isNightOwl(prefRequest.getIsNightOwl())
//                        .hasPet(prefRequest.getHasPet())
//                        .smokes(prefRequest.getSmokes())
//                        .oftenBringsFriendsOver(prefRequest.getOftenBringsFriendsOver())
//                        .roommatePost(post)
//                        .build();
//                roommatePreferenceRepository.save(preference);
//            });
//        }
//
//        // Lưu RoommatePost
//        RoommatePost updatedPost = roommatePostRepository.save(post);
//        return mapToResponse(updatedPost);
//    }
//
//    @Override
//    @Transactional
//    public void deleteRoommatePost(Long id, String userEmail) {
//        RoommatePost post = roommatePostRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("RoommatePost not found with id: " + id));
//
//        // Kiểm tra quyền sở hữu
//        if (!post.getOwnerPost().equals(userEmail)) {
//            throw new SecurityException("You are not authorized to delete this post");
//        }
//
//        roommatePostRepository.delete(post);
//    }
//
//    // Helper method to map RoommatePost to RoommatePostResponse
//    private RoommatePostResponse mapToResponse(RoommatePost post) {
//        RoommatePostResponse response = new RoommatePostResponse();
//        response.setId(post.getId());
//        response.setOwnerPost(post.getOwnerPost());
//        response.setAddress(post.getAddress());
//        response.setAreaSquareMeters(post.getAreaSquareMeters());
//        response.setMonthlyRentPrice(post.getMonthlyRentPrice());
//        response.setDescription(post.getDescription());
//        response.setCreatedDate(post.getCreatedDate());
//
//        // Map RoommatePreferences
//        List<RoommatePostResponse.RoommatePreferenceResponse> preferences = roommatePreferenceRepository
//                .findByRoommatePostId(post.getId())
//                .stream()
//                .map(pref -> new RoommatePostResponse.RoommatePreferenceResponse(
//                        pref.getId(),
//                        pref.getName(),
//                        pref.getDateOfBirth(),
//                        pref.getGender(),
//                        pref.getOccupation(),
//                        pref.getDescription(),
//                        pref.getPreferredPersonality(),
//                        pref.getCanCook(),
//                        pref.getIsNightOwl(),
//                        pref.getHasPet(),
//                        pref.getSmokes(),
//                        pref.getOftenBringsFriendsOver()
//                ))
//                .collect(Collectors.toList());
//        response.setRoommatePreferences(preferences);
//
//        return response;
//    }
//}