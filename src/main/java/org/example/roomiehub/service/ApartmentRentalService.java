package org.example.roomiehub.service;

import org.example.roomiehub.dto.request.ApartmentFilterRequest;
import org.example.roomiehub.dto.request.ApartmentRentalRequest;
import org.example.roomiehub.dto.response.ApartmentRentalResponse;

import java.util.List;

public interface ApartmentRentalService {

    ApartmentRentalResponse createApartmentRental(ApartmentRentalRequest request);

    ApartmentRentalResponse getApartmentRentalById(Long id);

    List<ApartmentRentalResponse> getAllApartmentRentals();

    ApartmentRentalResponse updateApartmentRental(Long id, ApartmentRentalRequest request);

    boolean deleteApartmentRental(Long id);

    List<ApartmentRentalResponse> filterApartments(ApartmentFilterRequest filter);

    List<ApartmentRentalResponse> searchApartmentsByKeyword(String keyword);

}
