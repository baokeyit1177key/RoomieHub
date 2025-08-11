package org.example.roomiehub.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApartmentRentalRequest {
    private String title;
    private String description;
    private String address;          // Địa chỉ dạng String
    private double price;
    private double area;
    private String genderRequirement;
    private String deposit;
    private String legalDocuments;
    private String utilities;
    private String furniture;
    private String interiorCondition;
    private String elevator;
    private String contact;
    private List<String> imageBase64s;
    private String location;
    private Long packageId;// Tọa độ dạng "latitude,longitude"
}
