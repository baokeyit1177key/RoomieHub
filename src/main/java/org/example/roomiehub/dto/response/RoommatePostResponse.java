package org.example.roomiehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.Enum.PersonalityTrait;
import org.example.roomiehub.Enum.YesNoAnswer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoommatePostResponse {
    private Long id;
    private String ownerPost;
    private String address;
    private double areaSquareMeters;
    private double monthlyRentPrice;
    private String description;
    private Long userId; // Chỉ trả về ID của user để tránh tải toàn bộ đối tượng User
    private List<RoommatePreferenceResponse> roommatePreferences = new ArrayList<>();
    private LocalDate createdDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoommatePreferenceResponse {
        private Long id;
        private String name;
        private LocalDate dateOfBirth;
        private Enums.Gender gender;
        private String occupation;
        private String description;
        private PersonalityTrait preferredPersonality;
        private YesNoAnswer canCook;
        private YesNoAnswer isNightOwl;
        private YesNoAnswer hasPet;
        private YesNoAnswer smokes;
        private YesNoAnswer oftenBringsFriendsOver;
    }
}