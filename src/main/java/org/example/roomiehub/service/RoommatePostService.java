package org.example.roomiehub.service;

import org.example.roomiehub.dto.request.RoommatePostRequest;
import org.example.roomiehub.dto.response.RoommatePostResponse;

import java.util.List;

public interface RoommatePostService {
    RoommatePostResponse createRoommatePost(RoommatePostRequest request, String userEmail);
    RoommatePostResponse getRoommatePostById(Long id);
    List<RoommatePostResponse> getAllRoommatePosts();
    RoommatePostResponse updateRoommatePost(Long id, RoommatePostRequest request, String userEmail);
    void deleteRoommatePost(Long id, String userEmail);
}