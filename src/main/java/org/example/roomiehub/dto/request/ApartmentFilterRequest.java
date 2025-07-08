package org.example.roomiehub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentFilterRequest {
    private Double minPrice;
    private Double maxPrice;
    private Double minArea;
    private Double maxArea;

    private String genderRequirement;
    private String deposit;
    private String legalDocuments;
    private String utilities;
    private String furniture;
    private String interiorCondition;
    private String elevator;
    private String contact;
    private String addressKeyword;
    private String descriptionKeyword;

    private String keyword; // Tổng hợp fuzzy keyword nếu muốn
}
