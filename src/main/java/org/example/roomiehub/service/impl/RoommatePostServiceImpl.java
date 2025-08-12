package org.example.roomiehub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.dto.request.RoommatePostFilterRequest;
import org.example.roomiehub.dto.request.RoommatePostRequest;
import org.example.roomiehub.dto.response.RoommatePostResponse;
import org.example.roomiehub.model.RoommatePost;
import org.example.roomiehub.model.RoommatePreference;
import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.model.User;
import org.example.roomiehub.repository.RoommatePostRepository;
import org.example.roomiehub.repository.RoommatePreferenceRepository;
import org.example.roomiehub.repository.SurveyRepository;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.RoommatePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoommatePostServiceImpl implements RoommatePostService {

    @Autowired
    private RoommatePostRepository postRepo;

    @Autowired
    private RoommatePreferenceRepository prefRepo;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    @Transactional
    public RoommatePostResponse createRoommatePost(RoommatePostRequest request, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        RoommatePost post = RoommatePost.builder()
                .userId(user.getId())
                .ownerPost(userEmail)
                .address(request.getAddress())
                .areaSquareMeters(request.getAreaSquareMeters())
                .monthlyRentPrice(request.getMonthlyRentPrice())
                .description(request.getDescription())
                .imageBase64List(request.getImageBase64List())
                .build();

        RoommatePost savedPost = postRepo.save(post);

        if (request.getRoommatePreferences() != null) {
            request.getRoommatePreferences().forEach(pref -> {
                RoommatePreference preference = RoommatePreference.builder()
                        .name(pref.getName())
                        .dateOfBirth(pref.getDateOfBirth())
                        .gender(pref.getGender())
                        .occupation(pref.getOccupation())
                        .description(pref.getDescription())
                        .preferredPersonality(pref.getPreferredPersonality())
                        .canCook(pref.getCanCook())
                        .isNightOwl(pref.getIsNightOwl())
                        .hasPet(pref.getHasPet())
                        .smokes(pref.getSmokes())
                        .oftenBringsFriendsOver(pref.getOftenBringsFriendsOver())
                        .roommatePost(savedPost)
                        .build();
                prefRepo.save(preference);
            });
        }

        return mapToResponse(savedPost);
    }

    @Override
    public RoommatePostResponse getRoommatePostById(Long id) {
        RoommatePost post = postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoommatePost not found with id: " + id));
        return mapToResponse(post);
    }

    @Override
    public List<RoommatePostResponse> getAllPosts() {
        return postRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoommatePostResponse> getUserRoommatePosts(String userEmail) {
        return postRepo.findByOwnerPost(userEmail)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoommatePostResponse updateRoommatePost(Long id, RoommatePostRequest request, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        RoommatePost post = postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoommatePost not found with id: " + id));

        if (post.getUserId() != user.getId()) {
            throw new SecurityException("You are not authorized to update this post");
        }

        post.setAddress(request.getAddress());
        post.setAreaSquareMeters(request.getAreaSquareMeters());
        post.setMonthlyRentPrice(request.getMonthlyRentPrice());
        post.setDescription(request.getDescription());
        post.setImageBase64List(request.getImageBase64List());

        prefRepo.deleteByRoommatePostId(id);

        if (request.getRoommatePreferences() != null) {
            request.getRoommatePreferences().forEach(pref -> {
                RoommatePreference preference = RoommatePreference.builder()
                        .name(pref.getName())
                        .dateOfBirth(pref.getDateOfBirth())
                        .gender(pref.getGender())
                        .occupation(pref.getOccupation())
                        .description(pref.getDescription())
                        .preferredPersonality(pref.getPreferredPersonality())
                        .canCook(pref.getCanCook())
                        .isNightOwl(pref.getIsNightOwl())
                        .hasPet(pref.getHasPet())
                        .smokes(pref.getSmokes())
                        .oftenBringsFriendsOver(pref.getOftenBringsFriendsOver())
                        .roommatePost(post)
                        .build();
                prefRepo.save(preference);
            });
        }

        return mapToResponse(postRepo.save(post));
    }

    @Override
    @Transactional
    public void deleteRoommatePost(Long id, String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + userEmail));

        RoommatePost post = postRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoommatePost not found with id: " + id));

        if (post.getUserId() != user.getId()) {
            throw new SecurityException("You are not authorized to delete this post");
        }

        postRepo.delete(post);
    }

    private RoommatePostResponse mapToResponse(RoommatePost post) {
        RoommatePostResponse response = RoommatePostResponse.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .ownerPost(post.getOwnerPost())
                .address(post.getAddress())
                .areaSquareMeters(post.getAreaSquareMeters())
                .monthlyRentPrice(post.getMonthlyRentPrice())
                .description(post.getDescription())
                .createdDate(post.getCreatedDate())
                .imageBase64List(post.getImageBase64List())
                .build();

        List<RoommatePostResponse.RoommatePreferenceResponse> preferences =
                prefRepo.findByRoommatePostId(post.getId())
                        .stream()
                        .map(pref -> RoommatePostResponse.RoommatePreferenceResponse.builder()
                                .id(pref.getId())
                                .name(pref.getName())
                                .dateOfBirth(pref.getDateOfBirth())
                                .gender(pref.getGender())
                                .occupation(pref.getOccupation())
                                .description(pref.getDescription())
                                .preferredPersonality(pref.getPreferredPersonality())
                                .canCook(pref.getCanCook())
                                .isNightOwl(pref.getIsNightOwl())
                                .hasPet(pref.getHasPet())
                                .smokes(pref.getSmokes())
                                .oftenBringsFriendsOver(pref.getOftenBringsFriendsOver())
                                .build())
                        .collect(Collectors.toList());

        response.setRoommatePreferences(preferences);
        return response;
    }

    @Override
    public List<RoommatePostResponse> filterRoommatePosts(RoommatePostFilterRequest filterRequest) {
        return postRepo.findAll()
                .stream()
                .filter(post -> {
                    boolean matchesPost = (filterRequest.getAddress() == null ||
                            post.getAddress().toLowerCase().contains(filterRequest.getAddress().toLowerCase())) &&
                            (filterRequest.getMinArea() == null ||
                                    post.getAreaSquareMeters() >= filterRequest.getMinArea()) &&
                            (filterRequest.getMaxPrice() == null ||
                                    post.getMonthlyRentPrice() <= filterRequest.getMaxPrice());

                    if (!matchesPost) return false;

                    return post.getRoommatePreferences().stream().anyMatch(pref ->
                            (filterRequest.getDob() == null ||
                                    pref.getDateOfBirth().equals(filterRequest.getDob())) &&
                                    (filterRequest.getGender() == null ||
                                            pref.getGender() == filterRequest.getGender()) &&
                                    (filterRequest.getOccupation() == null ||
                                            pref.getOccupation().toLowerCase().contains(filterRequest.getOccupation().toLowerCase())) &&
                                    (filterRequest.getPersonality() == null ||
                                            pref.getPreferredPersonality() == filterRequest.getPersonality()) &&
                                    (filterRequest.getCanCook() == null ||
                                            pref.getCanCook() == filterRequest.getCanCook()) &&
                                    (filterRequest.getIsNightOwl() == null ||
                                            pref.getIsNightOwl() == filterRequest.getIsNightOwl()) &&
                                    (filterRequest.getHasPet() == null ||
                                            pref.getHasPet() == filterRequest.getHasPet()) &&
                                    (filterRequest.getSmokes() == null ||
                                            pref.getSmokes() == filterRequest.getSmokes()) &&
                                    (filterRequest.getBringsFriends() == null ||
                                            pref.getOftenBringsFriendsOver() == filterRequest.getBringsFriends())
                    );
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

@Override
@Transactional(readOnly = true)
public List<RoommatePostResponse> recommendRoommatePosts(String userEmail) {
    SurveyAnswer survey = surveyRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotFoundException("SurveyAnswer not found for email: " + userEmail));

    Enums.Gender gender = survey.getGender();
    if (gender == null) {
        throw new IllegalArgumentException("SurveyAnswer must have gender set");
    }

    double userPrice = survey.getPrice() != null ? survey.getPrice() : 0;
    double minPrice = Math.max(0, userPrice - 1_000_000);
    double maxPrice = userPrice + 1_000_000;

    // B1: Lấy toàn bộ bài đăng theo gender + price range
    List<RoommatePost> initialPosts = postRepo.findByGenderAndPriceRange(gender, minPrice, maxPrice);

    // B2: Lọc theo địa chỉ nếu có
    String desiredAddress = survey.getLocation();
    List<RoommatePost> filteredByAddress = initialPosts;

    if (desiredAddress != null && !desiredAddress.trim().isEmpty()) {
        String[] keywords = desiredAddress.split(",");
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = keywords[i].trim().toLowerCase();
        }

        filteredByAddress = initialPosts.stream()
                .filter(post -> {
                    if (post.getAddress() == null) return false;
                    String addressLower = post.getAddress().toLowerCase();
                    for (String keyword : keywords) {
                        if (addressLower.contains(keyword)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

        // B3: Nếu lọc địa chỉ ra 0 kết quả → bỏ qua bước lọc địa chỉ
        if (filteredByAddress.isEmpty()) {
            filteredByAddress = initialPosts;
        }
    }

    // B4: Map sang response
    return filteredByAddress.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
}


}
