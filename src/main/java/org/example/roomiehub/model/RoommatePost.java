package org.example.roomiehub.model;

import jakarta.persistence.*; // Annotation của JPA để ánh xạ Entity với DB
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
    private Long id; // ID duy nhất của bài đăng

    private String ownerPost;

    private String address; // Địa chỉ của căn nhà
    private double areaSquareMeters; // Diện tích căn nhà (m2)
    private double monthlyRentPrice; // Giá thuê hàng tháng

    @ElementCollection
    @CollectionTable(name = "roommate_post_images", joinColumns = @JoinColumn(name = "roommate_post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Column(columnDefinition = "TEXT") // Kiểu TEXT trong DB cho mô tả dài
    private String description; // Mô tả chi tiết về căn nhà


    @OneToMany(mappedBy = "roommatePost", // Mối quan hệ một-đến-nhiều với RoommatePreference
               cascade = CascadeType.ALL, // Mọi thao tác trên RoommatePost sẽ áp dụng cho RoommatePreference liên quan
               orphanRemoval = true)      // Tự động xóa RoommatePreference nếu nó bị gỡ khỏi danh sách
    private List<RoommatePreference> roommatePreferences = new ArrayList<>(); // Danh sách các yêu cầu về bạn cùng phòng

    private LocalDate createdDate; // Ngày bài đăng được tạo

    @PrePersist // Phương thức này được gọi trước khi Entity được lưu lần đầu
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDate.now(); // Gán ngày hiện tại
        }
    }
}