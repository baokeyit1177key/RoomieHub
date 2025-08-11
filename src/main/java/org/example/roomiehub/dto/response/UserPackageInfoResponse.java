package org.example.roomiehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomiehub.Enum.PackageType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPackageInfoResponse    {
    private Long packageId;          // ID của gói
    private String packageName;      // Tên gói
    private int remainingPosts;      // Số lượt đăng còn lại
    private LocalDate startDate;     // Ngày bắt đầu
    private LocalDate expiredAt;


}
