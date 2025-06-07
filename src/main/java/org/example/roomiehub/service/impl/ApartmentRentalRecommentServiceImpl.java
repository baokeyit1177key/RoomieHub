package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.model.ApartmentRental;
import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.repository.ApartmentRentalRecommentRepository;
import org.example.roomiehub.repository.SurveyRepository;
import org.example.roomiehub.service.ApartmentRentalRecommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentRentalRecommentServiceImpl implements ApartmentRentalRecommentService {

    private final SurveyRepository surveyRepository;
    private final ApartmentRentalRecommentRepository apartmentRentalRecommentRepository;

    @Override
    public List<Long> recommendApartmentIdsForLoggedUser() {
        // Lấy email đang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Tìm SurveyAnswer của user theo email
        SurveyAnswer survey = surveyRepository.findByEmail(email).orElse(null);
        if (survey == null) {
            return List.of(); // Không có survey nào
        }

        String[] latLong = survey.getLocation().split(",");
        double latitude = Double.parseDouble(latLong[0].trim());
        double longitude = Double.parseDouble(latLong[1].trim());

        // Tìm các apartment phù hợp
        List<ApartmentRental> suitableApartments = apartmentRentalRecommentRepository.findSuitableApartments(
                survey.getPrice(),
                survey.getArea(),
                survey.getGenderRequiment().name(),
                survey.getDeposit(),
                survey.getUtilities().name(),
                survey.getFurniture().name(),
                latitude,
                longitude
        );

        // Nếu không tìm thấy, tìm random theo giá gần nhất (chênh lệch < 3 triệu)
        if (suitableApartments.isEmpty()) {
            suitableApartments = apartmentRentalRecommentRepository.findRandomByPriceClose(survey.getPrice());
        }

        // Trả ra danh sách id căn hộ
        return suitableApartments.stream()
                .map(ApartmentRental::getId)
                .collect(Collectors.toList());
    }
}
