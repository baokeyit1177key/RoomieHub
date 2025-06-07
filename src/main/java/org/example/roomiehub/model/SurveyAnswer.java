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
@Builder // Thêm annotation này để có builder()
@Entity
@Table(name = "survey_answer")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thông tin người dùng
    private String userName;
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

    // Giá thuê (VNĐ)
private double price;

// Diện tích phòng (m²)
private double area;

// Yêu cầu về giới tính người thuê (Nam, Nữ, Không yêu cầu)
@Enumerated(EnumType.STRING)
private GenderLevel genderRequiment;

// Tiền cọc (có thể là số tiền hoặc mô tả như "1 tháng", "2 tháng")
private double deposit;

@Enumerated(EnumType.STRING)
private UtilitiesLevel utilities;

@Enumerated(EnumType.STRING)
private FurnitureLevel furniture;

// Vị trí địa lý dạng "latitude,longitude" (tọa độ GPS)
private String location;
}
