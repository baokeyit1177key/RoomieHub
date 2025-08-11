package org.example.roomiehub.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.roomiehub.dto.request.RoommatePostFilterRequest;
import org.example.roomiehub.dto.request.RoommatePostRequest;
import org.example.roomiehub.dto.response.RoommatePostResponse;
import org.example.roomiehub.model.RoommatePost;
import org.example.roomiehub.model.RoommatePreference;
import org.example.roomiehub.model.User;
import org.example.roomiehub.repository.RoommatePostRepository;
import org.example.roomiehub.repository.RoommatePreferenceRepository;
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
}
