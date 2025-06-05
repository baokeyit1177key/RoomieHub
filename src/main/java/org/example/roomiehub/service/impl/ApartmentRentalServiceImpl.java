package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.ApartmentRentalRequest;
import org.example.roomiehub.dto.response.ApartmentRentalResponse;
import org.example.roomiehub.model.ApartmentRental;
import org.example.roomiehub.model.User;
import org.example.roomiehub.repository.ApartmentRentalRepository;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.ApartmentRentalService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentRentalServiceImpl implements ApartmentRentalService {

    private final ApartmentRentalRepository repository;
    private final UserRepository userRepository;

    @Override
    public ApartmentRentalResponse createApartmentRental(ApartmentRentalRequest request) {
        Long userId = getCurrentUserIdByEmail();

        ApartmentRental apartment = ApartmentRental.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .price(request.getPrice())
                .area(request.getArea())
                .genderRequirement(request.getGenderRequirement())
                .deposit(request.getDeposit())
                .legalDocuments(request.getLegalDocuments())
                .utilities(request.getUtilities())
                .furniture(request.getFurniture())
                .interiorCondition(request.getInteriorCondition())
                .elevator(request.getElevator())
                .contact(request.getContact())
                .imageUrls(request.getImageUrls())
                .location(request.getLocation())
                .userId(userId)
                .build();

        ApartmentRental savedApartment = repository.save(apartment);
        return mapToResponse(savedApartment);
    }

    @Override
    public ApartmentRentalResponse getApartmentRentalById(Long id) {
        Optional<ApartmentRental> apartment = repository.findById(id);
        return apartment.map(this::mapToResponse).orElse(null);
    }

    @Override
    public List<ApartmentRentalResponse> getAllApartmentRentals() {
        List<ApartmentRental> apartments = repository.findAll();
        return apartments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApartmentRentalResponse updateApartmentRental(Long id, ApartmentRentalRequest request) {
        Optional<ApartmentRental> optionalApartment = repository.findById(id);
        if (optionalApartment.isPresent()) {
            ApartmentRental apartment = optionalApartment.get();
            apartment.setTitle(request.getTitle());
            apartment.setDescription(request.getDescription());
            apartment.setAddress(request.getAddress());
            apartment.setPrice(request.getPrice());
            apartment.setArea(request.getArea());
            apartment.setGenderRequirement(request.getGenderRequirement());
            apartment.setDeposit(request.getDeposit());
            apartment.setLegalDocuments(request.getLegalDocuments());
            apartment.setUtilities(request.getUtilities());
            apartment.setFurniture(request.getFurniture());
            apartment.setInteriorCondition(request.getInteriorCondition());
            apartment.setElevator(request.getElevator());
            apartment.setContact(request.getContact());
            apartment.setImageUrls(request.getImageUrls());
            apartment.setLocation(request.getLocation());

            ApartmentRental updatedApartment = repository.save(apartment);
            return mapToResponse(updatedApartment);
        }
        return null;
    }

    @Override
    public boolean deleteApartmentRental(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private ApartmentRentalResponse mapToResponse(ApartmentRental apartment) {
        return ApartmentRentalResponse.builder()
                .id(apartment.getId())
                .title(apartment.getTitle())
                .description(apartment.getDescription())
                .address(apartment.getAddress())
                .price(apartment.getPrice())
                .area(apartment.getArea())
                .genderRequirement(apartment.getGenderRequirement())
                .deposit(apartment.getDeposit())
                .legalDocuments(apartment.getLegalDocuments())
                .utilities(apartment.getUtilities())
                .furniture(apartment.getFurniture())
                .interiorCondition(apartment.getInteriorCondition())
                .elevator(apartment.getElevator())
                .contact(apartment.getContact())
                .imageUrls(apartment.getImageUrls())
                .location(apartment.getLocation())
                .userId(apartment.getUserId())
                .build();
    }

    // Lấy userId bằng email user đang đăng nhập
    private Long getCurrentUserIdByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return user.getId();
    }
}
