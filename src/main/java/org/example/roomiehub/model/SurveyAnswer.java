package org.example.roomiehub.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.Enum.FurnitureLevel;
import org.example.roomiehub.Enum.GenderLevel;
import org.example.roomiehub.Enum.UtilitiesLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "survey_answer")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private Integer birthYear;
    private String email;

    @Enumerated(EnumType.STRING)
    private Enums.Hometown hometown;

    @Enumerated(EnumType.STRING)
    private Enums.Gender gender;

    @Enumerated(EnumType.STRING)
    private Enums.Occupation occupation;

    @Enumerated(EnumType.STRING)
    private Enums.PriceRange priceRange;

    private Double currentLatitude;
    private Double currentLongitude;

    @Enumerated(EnumType.STRING)
    private Enums.PreferredLocation preferredLocation;

    @Enumerated(EnumType.STRING)
    private Enums.YesNo smoking;

    @Enumerated(EnumType.STRING)
    private Enums.YesNo pets;

    @Enumerated(EnumType.STRING)
    private Enums.CookFrequency cookFrequency;

    @Enumerated(EnumType.STRING)
    private Enums.SleepHabit sleepHabit;

    @Enumerated(EnumType.STRING)
    private Enums.YesNo inviteFriends;

    private Double price;
    private Double area;

    @Enumerated(EnumType.STRING)
    private GenderLevel genderRequiment;

    private Double deposit;

    @Enumerated(EnumType.STRING)
    private UtilitiesLevel utilities;

    @Enumerated(EnumType.STRING)
    private FurnitureLevel furniture;

    private String location;
}
