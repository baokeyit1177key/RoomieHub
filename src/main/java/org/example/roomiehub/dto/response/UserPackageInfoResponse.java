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
    PackageType packageType;
    int remainingPosts;
    boolean isVrSupported;
    LocalDate expiredAt;
}
