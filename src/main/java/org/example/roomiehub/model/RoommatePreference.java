package org.example.roomiehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomiehub.Enum.Enums; // Import các Enum đã định nghĩa
import org.example.roomiehub.Enum.PersonalityTrait;
import org.example.roomiehub.Enum.YesNoAnswer;

import java.time.LocalDate;

@Entity // Đánh dấu đây là Entity
@Table(name = "roommate_preferences") // Ánh xạ tới bảng "roommate_preferences"
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
        public class RoommatePreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID duy nhất của yêu cầu bạn cùng phòng

    private String name; // Tên của người tìm phòng (hoặc tên đại diện)

    private LocalDate dateOfBirth; // Năm sinh của người tìm phòng

    @Enumerated(EnumType.STRING) // Lưu Enum dưới dạng chuỗi trong DB
    private Enums.Gender gender; // Giới tính

    private String occupation; // Công việc

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả chung về bản thân

    @Enumerated(EnumType.STRING)
    private PersonalityTrait preferredPersonality; // Yêu cầu tính cách bạn cùng nhà

    @Enumerated(EnumType.STRING)
    private YesNoAnswer canCook; // Có nấu ăn không

    @Enumerated(EnumType.STRING)
    private YesNoAnswer isNightOwl; // Có thức khuya không

    @Enumerated(EnumType.STRING)
    private YesNoAnswer hasPet; // Có nuôi pet không

    @Enumerated(EnumType.STRING)
    private YesNoAnswer smokes; // Có hút thuốc lá không

    @Enumerated(EnumType.STRING)
    private YesNoAnswer oftenBringsFriendsOver; // Có hay dẫn bạn về chơi không

    @ManyToOne(fetch = FetchType.LAZY) // Mối quan hệ nhiều-đến-một với RoommatePost
    @JoinColumn(name = "roommate_post_id") // Cột khóa ngoại 'roommate_post_id'
    private RoommatePost roommatePost; // Bài đăng mà yêu cầu này thuộc về
}