package org.example.roomiehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomiehub.Enum.Enums;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder   // Thêm annotation này để có builder()
@Entity
@Table (name = "survey_answer")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int userId;

    private int birthYear;

    private String email;

    @Enumerated(EnumType.STRING)
    private Enums.Hometown hometown;

    @Enumerated(EnumType.STRING)
    private Enums.Gender gender;

    @Enumerated(EnumType.STRING)
    private Enums.Occupation occupation;

    @Enumerated(EnumType.STRING)
    private Enums.PriceRange priceRange;

    private double currentLatitude;

    private double currentLongitude;

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
}