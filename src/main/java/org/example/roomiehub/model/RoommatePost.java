package org.example.roomiehub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roommate_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoommatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;


    private String ownerPost;
    private String address;
    private double areaSquareMeters;
    private double monthlyRentPrice;

    private Double latitude;
    private Double longitude;


    // Lưu danh sách ảnh dưới dạng Base64 string
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "roommate_post_images", joinColumns = @JoinColumn(name = "roommate_post_id"))
    @Column(name = "image_base64", columnDefinition = "LONGTEXT") // LONGTEXT để chứa Base64 dài
    private List<String> imageBase64List = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "roommatePost",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<RoommatePreference> roommatePreferences = new ArrayList<>();

    private LocalDate createdDate;

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDate.now();
        }
    }
}
