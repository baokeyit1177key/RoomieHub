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

    // Phần tìm phòng (áp dụng với việc tìm thuê hoặc tìm ở ghép)
    private Double price;                     // Giá thuê
    private Double area;                      // Diện tích
    private GenderLevel genderRequiment;      // Giới tính yêu cầu
    private Double deposit;                   // Tiền cọc
    private UtilitiesLevel utilities;         // Tiện ích
    private FurnitureLevel furniture;         // Nội thất
    private String location;                  // Tọa độ "latitude,longitude"
}
