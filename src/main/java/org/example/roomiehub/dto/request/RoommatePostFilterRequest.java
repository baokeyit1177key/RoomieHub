package org.example.roomiehub.dto.request;


import lombok.Data;
import org.example.roomiehub.Enum.Enums.Gender;
import org.example.roomiehub.Enum.PersonalityTrait;
import org.example.roomiehub.Enum.YesNoAnswer;

import java.time.LocalDate;

@Data
public class RoommatePostFilterRequest {
    // Lọc theo RoommatePost
    private String address;
    private Double minArea;
    private Double maxPrice;

    // Lọc theo RoommatePreference
    private LocalDate dob;
    private Gender gender;
    private String occupation;
    private PersonalityTrait personality;
    private YesNoAnswer canCook;
    private YesNoAnswer isNightOwl;
    private YesNoAnswer hasPet;
    private YesNoAnswer smokes;
    private YesNoAnswer bringsFriends;
}
