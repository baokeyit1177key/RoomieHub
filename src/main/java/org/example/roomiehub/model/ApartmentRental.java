package org.example.roomiehub.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "apartment_rentals")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ApartmentRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String address;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "apartment_images_base64", joinColumns = @JoinColumn(name = "apartment_id"))
    @Column(name = "image_base64", columnDefinition = "LONGTEXT")
    private List<String> imageBase64s; // Lưu list ảnh base64

    private long userId;

    private String location; // "latitude,longitude"
}