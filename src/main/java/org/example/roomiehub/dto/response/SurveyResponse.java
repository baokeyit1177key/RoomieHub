package org.example.roomiehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomiehub.Enum.Enums;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    private Long id;
    private Integer birthYear;
    private Enums.Hometown  hometown;
    private Enums.Gender  gender;
    private Enums.Occupation occupation;
    private Enums.PriceRange priceRange;
    private Double currentLatitude;
    private Double currentLongitude;
    private Enums.PreferredLocation  preferredLocation;
    private Enums.YesNo  smoking;
    private Enums.YesNo  pets;
    private Enums.CookFrequency  cookFrequency;
    private Enums.SleepHabit  sleepHabit;
    private Enums.YesNo  inviteFriends;
}