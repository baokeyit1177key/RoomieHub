package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.example.roomiehub.dto.request.ApartmentFilterRequest;
import org.example.roomiehub.dto.request.ApartmentRentalRequest;
import org.example.roomiehub.dto.response.ApartmentRentalResponse;
import org.example.roomiehub.exception.NoActivePackageException;
import org.example.roomiehub.exception.OutOfPostQuotaException;
import org.example.roomiehub.model.ApartmentRental;
import org.example.roomiehub.model.User;
import org.example.roomiehub.model.UserPackage;
import org.example.roomiehub.repository.ApartmentRentalRepository;
import org.example.roomiehub.repository.UserPackageRepository;
import org.example.roomiehub.repository.UserRepository;
import org.example.roomiehub.service.ApartmentRentalService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentRentalServiceImpl implements ApartmentRentalService {

    private final ApartmentRentalRepository repository;
    private final UserRepository userRepository;
    private final UserPackageRepository userPackageRepository;

    @Override
    public ApartmentRentalResponse createApartmentRental(ApartmentRentalRequest request) {
        Long userId = getCurrentUserIdByEmail();
        User user = userRepository.findById(userId).orElseThrow();

        UserPackage selectedPackage = userPackageRepository
                .findByIdAndUserIdAndActiveTrue(request.getPackageId(), userId)
                .orElseThrow(() -> new NoActivePackageException("Không tìm thấy gói hoặc gói đã hết hạn"));

        if (selectedPackage.getEndDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Gói này đã hết hạn");
        }

        if (selectedPackage.getRemainingPosts() <= 0) {
            throw new OutOfPostQuotaException("Gói này đã hết lượt đăng bài");
        }

        selectedPackage.setRemainingPosts(selectedPackage.getRemainingPosts() - 1);
        userPackageRepository.save(selectedPackage);

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
                .imageBase64s(request.getImageBase64s())  // Sửa đây
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
            apartment.setImageBase64s(request.getImageBase64s());  // Sửa đây
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
                .imageBase64s(apartment.getImageBase64s())  // Sửa đây
                .location(apartment.getLocation())
                .userId(apartment.getUserId())
                .build();
    }

    public Long getCurrentUserIdByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return user.getId();
    }

    @Override
    public List<ApartmentRentalResponse> filterApartments(ApartmentFilterRequest filter) {
        List<ApartmentRental> apartments = repository.findAll();

        String keyword = filter.getKeyword() != null ? filter.getKeyword().toLowerCase() : null;
        LevenshteinDistance distanceCalculator = new LevenshteinDistance();
        int threshold = 3;

        return apartments.stream()
                .filter(a -> filter.getMinPrice() == null || a.getPrice() >= filter.getMinPrice())
                .filter(a -> filter.getMaxPrice() == null || a.getPrice() <= filter.getMaxPrice())
                .filter(a -> filter.getMinArea() == null || a.getArea() >= filter.getMinArea())
                .filter(a -> filter.getMaxArea() == null || a.getArea() <= filter.getMaxArea())
                .filter(a -> isMatch(filter.getGenderRequirement(), a.getGenderRequirement()))
                .filter(a -> isMatch(filter.getDeposit(), a.getDeposit()))
                .filter(a -> isMatch(filter.getLegalDocuments(), a.getLegalDocuments()))
                .filter(a -> isMatch(filter.getUtilities(), a.getUtilities()))
                .filter(a -> isMatch(filter.getFurniture(), a.getFurniture()))
                .filter(a -> isMatch(filter.getInteriorCondition(), a.getInteriorCondition()))
                .filter(a -> isMatch(filter.getElevator(), a.getElevator()))
                .filter(a -> isMatch(filter.getContact(), a.getContact()))
                .filter(a -> filter.getAddressKeyword() == null || a.getAddress().toLowerCase().contains(filter.getAddressKeyword().toLowerCase()))
                .filter(a -> filter.getDescriptionKeyword() == null || a.getDescription().toLowerCase().contains(filter.getDescriptionKeyword().toLowerCase()))
                .filter(a -> {
                    if (keyword == null || keyword.isBlank()) return true;
                    return isFuzzyMatch(a.getTitle(), keyword, distanceCalculator, threshold)
                            || isFuzzyMatch(a.getDescription(), keyword, distanceCalculator, threshold)
                            || isFuzzyMatch(a.getAddress(), keyword, distanceCalculator, threshold);
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private boolean isMatch(String expected, String actual) {
        return expected == null || expected.isBlank()
                || (actual != null && actual.toLowerCase().contains(expected.toLowerCase()));
    }

    @Override
    public List<ApartmentRentalResponse> searchApartmentsByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String lowerKeyword = keyword.toLowerCase();
        List<ApartmentRental> apartments = repository.findAll();

        LevenshteinDistance distanceCalculator = new LevenshteinDistance();
        int threshold = 3;

        return apartments.stream()
                .filter(a -> isFuzzyMatch(a.getTitle(), lowerKeyword, distanceCalculator, threshold)
                        || isFuzzyMatch(a.getDescription(), lowerKeyword, distanceCalculator, threshold)
                        || isFuzzyMatch(a.getAddress(), lowerKeyword, distanceCalculator, threshold))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private boolean isFuzzyMatch(String text, String keyword, LevenshteinDistance distanceCalculator, int threshold) {
        if (text == null || keyword == null) return false;

        String[] words = text.toLowerCase().split("\\s+");

        for (String word : words) {
            Integer distance = distanceCalculator.apply(word, keyword);
            if (distance != null && distance <= threshold) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<ApartmentRentalResponse> findNearbyApartments(double lat, double lon, double radiusKm) {
        List<ApartmentRental> apartments = repository.findAll();

        return apartments.stream()
                .filter(a -> a.getLocation() != null && a.getLocation().contains(","))
                .filter(a -> {
                    String[] parts = a.getLocation().split(",");
                    try {
                        double aptLat = Double.parseDouble(parts[0].trim());
                        double aptLon = Double.parseDouble(parts[1].trim());
                        double distance = haversine(lat, lon, aptLat, aptLon);
                        return distance <= radiusKm;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public long countAllApartments() {
        return repository.count();
    }

    @Override
    public long countMyApartments() {
        Long userId = getCurrentUserIdByEmail();
        return repository.countByUserId(userId);
    }

    @Override
    public List<ApartmentRentalResponse> getMyApartments() {
        Long userId = getCurrentUserIdByEmail();
        List<ApartmentRental> myApartments = repository.findByUserId(userId);
        return myApartments.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
