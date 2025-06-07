package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.service.ApartmentRentalRecommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartment-recommendation")
@RequiredArgsConstructor
public class ApartmentRentalRecommentController {

    private final ApartmentRentalRecommentService apartmentRentalRecommentService;

    @GetMapping("/suggestions")
    public ResponseEntity<?> recommendApartmentsForLoggedUser() {
        List<Long> recommendedApartmentIds = apartmentRentalRecommentService.recommendApartmentIdsForLoggedUser();

        if (recommendedApartmentIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không có căn hộ phù hợp với yêu cầu của bạn!");
        }

        return ResponseEntity.ok(recommendedApartmentIds);
    }
}
