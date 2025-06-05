package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.ApartmentRentalRequest;
import org.example.roomiehub.dto.response.ApartmentRentalResponse;
import org.example.roomiehub.service.ApartmentRentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentRentalController {

    private final ApartmentRentalService apartmentRentalService;

    // Tạo mới apartment rental
    @PostMapping
    public ResponseEntity<ApartmentRentalResponse> createApartmentRental(
            @RequestBody ApartmentRentalRequest request) {
        ApartmentRentalResponse response = apartmentRentalService.createApartmentRental(request);
        return ResponseEntity.ok(response);
    }

    // Lấy apartment theo id
    @GetMapping("/{id}")
    public ResponseEntity<ApartmentRentalResponse> getApartmentRentalById(@PathVariable Long id) {
        ApartmentRentalResponse response = apartmentRentalService.getApartmentRentalById(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    // Lấy tất cả apartments
    @GetMapping
    public ResponseEntity<List<ApartmentRentalResponse>> getAllApartmentRentals() {
        List<ApartmentRentalResponse> responses = apartmentRentalService.getAllApartmentRentals();
        return ResponseEntity.ok(responses);
    }

    // Cập nhật apartment theo id
    @PutMapping("/{id}")
    public ResponseEntity<ApartmentRentalResponse> updateApartmentRental(
            @PathVariable Long id,
            @RequestBody ApartmentRentalRequest request) {
        ApartmentRentalResponse response = apartmentRentalService.updateApartmentRental(id, request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa apartment theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartmentRental(@PathVariable Long id) {
        boolean deleted = apartmentRentalService.deleteApartmentRental(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
