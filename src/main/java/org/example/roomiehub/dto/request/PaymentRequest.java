package org.example.roomiehub.dto.request;

import lombok.Data;
import org.example.roomiehub.Enum.PackageType;

@Data
public class PaymentRequest {
    private PackageType packageType;
    private String description;
}
