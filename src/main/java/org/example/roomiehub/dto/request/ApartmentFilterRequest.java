package org.example.roomiehub.dto.request;

import lombok.Data;

@Data
public class ApartmentFilterRequest {
    private String keyword;
    private Double minPrice;
    private Double maxPrice;
    private Double minArea;
    private Double maxArea;
    private String genderRequirement;
    private Boolean hasElevator;       // true = có thang máy, false = không
    private String addressKeyword;     // ví dụ: "quận 1", "Hồ Chí Minh"
}
