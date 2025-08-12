package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.response.ApartmentRentalResponse;
import org.example.roomiehub.service.ApartmentRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/apartment-recommendation")
@RequiredArgsConstructor
public class ApartmentRentalRecommentController {

    private final ApartmentRentalService apartmentRentalService;

    @GetMapping("/matching")
    public ResponseEntity<List<ApartmentRentalResponse>> getMatchingApartmentsForUser() {
        List<ApartmentRentalResponse> matchedApartments = apartmentRentalService.findMatchingApartmentsForLoggedInUser();
        return ResponseEntity.ok(matchedApartments);
    }
}
