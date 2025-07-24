package org.example.roomiehub.dto.response;

import lombok.*;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.Enum.FurnitureLevel;
import org.example.roomiehub.Enum.GenderLevel;
import org.example.roomiehub.Enum.UtilitiesLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    private Long id;
    private String userName;
    private Integer birthYear;
    private Enums.Hometown hometown;
    private Enums.Gender gender;
    private Enums.Occupation occupation;
    private Enums.PriceRange priceRange;
    private Double currentLatitude;
    private Double currentLongitude;
    private Enums.PreferredLocation preferredLocation;
    private Enums.YesNo smoking;
    private Enums.YesNo pets;
    private Enums.CookFrequency cookFrequency;
    private Enums.SleepHabit sleepHabit;
    private Enums.YesNo inviteFriends;

    // Phần tìm phòng
    private Double price;
    private Double area;
    private GenderLevel genderRequiment;
    private Double deposit;
    private UtilitiesLevel utilities;
    private FurnitureLevel furniture;
    private String location;
}
