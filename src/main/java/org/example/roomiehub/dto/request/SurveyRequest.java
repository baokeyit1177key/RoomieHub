package org.example.roomiehub.dto.request;

import lombok.*;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.Enum.FurnitureLevel;
import org.example.roomiehub.Enum.GenderLevel;
import org.example.roomiehub.Enum.UtilitiesLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyRequest {
    private Integer birthYear;                            // có thể null
    private String userName;                              // có thể null
    private Enums.Hometown hometown;                      // có thể null
    private Enums.Gender gender;                          // có thể null
    private Enums.Occupation occupation;                  // có thể null
    private Enums.PriceRange priceRange;                  // có thể null
    private Double currentLatitude;                       // có thể null
    private Double currentLongitude;                      // có thể null
    private Enums.PreferredLocation preferredLocation;    // có thể null
    private Enums.YesNo smoking;                          // có thể null
    private Enums.YesNo pets;                             // có thể null
    private Enums.CookFrequency cookFrequency;            // có thể null
    private Enums.SleepHabit sleepHabit;                  // có thể null
    private Enums.YesNo inviteFriends;                    // có thể null

    private Double price;                                 // có thể null
    private Double area;                                  // có thể null
    private GenderLevel genderRequiment;                  // có thể null
    private Double deposit;                               // có thể null
    private UtilitiesLevel utilities;                     // có thể null
    private FurnitureLevel furniture;                     // có thể null
    private String location;                              // có thể null
}
