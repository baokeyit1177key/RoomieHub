package org.example.roomiehub.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.roomiehub.Enum.PackageType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Quan hệ N:1 với User
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    private int totalPosts;     // Tổng số bài được đăng theo gói
    private int remainingPosts; // Số bài còn lại

    private boolean isVrSupported; // nếu gói là VR

    private boolean active;

    private LocalDate startDate;
    private LocalDate endDate;
}


