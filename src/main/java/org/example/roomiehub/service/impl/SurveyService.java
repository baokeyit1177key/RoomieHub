package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.SurveyRequest;
import org.example.roomiehub.dto.response.SurveyResponse;
import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.repository.SurveyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository repository;

    public SurveyResponse submitSurvey(SurveyRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Kiểm tra nếu email đã có survey
    if (repository.findByEmail(email).isPresent()) {
        return null; // Cho controller biết để xử lý lỗi
    }

    SurveyAnswer survey = SurveyAnswer.builder()
            .userName(request.getUserName())
            .email(email)
            .birthYear(request.getBirthYear())
            .hometown(request.getHometown())
            .gender(request.getGender())
            .occupation(request.getOccupation())
            .priceRange(request.getPriceRange())
            .currentLatitude(request.getCurrentLatitude())
            .currentLongitude(request.getCurrentLongitude())
            .preferredLocation(request.getPreferredLocation())
            .smoking(request.getSmoking())
            .pets(request.getPets())
            .cookFrequency(request.getCookFrequency())
            .sleepHabit(request.getSleepHabit())
            .inviteFriends(request.getInviteFriends())
            .price(request.getPrice())
            .area(request.getArea())
            .genderRequiment(request.getGenderRequiment())
            .deposit(request.getDeposit())
            .utilities(request.getUtilities())
            .furniture(request.getFurniture())
            .location(request.getLocation())
            .build();

    SurveyAnswer savedSurvey = repository.save(survey);
    return mapToResponse(savedSurvey);
}
    private SurveyResponse mapToResponse(SurveyAnswer survey) {
        return SurveyResponse.builder()
                .id(survey.getId())
                .userName(survey.getUserName())
                .birthYear(survey.getBirthYear())
                .hometown(survey.getHometown())
                .gender(survey.getGender())
                .occupation(survey.getOccupation())
                .priceRange(survey.getPriceRange())
                .currentLatitude(survey.getCurrentLatitude())
                .currentLongitude(survey.getCurrentLongitude())
                .preferredLocation(survey.getPreferredLocation())
                .smoking(survey.getSmoking())
                .pets(survey.getPets())
                .cookFrequency(survey.getCookFrequency())
                .sleepHabit(survey.getSleepHabit())
                .inviteFriends(survey.getInviteFriends())

                // Các field mới
                .price(survey.getPrice())
                .area(survey.getArea())
                .genderRequiment(survey.getGenderRequiment())
                .deposit(survey.getDeposit())
                .utilities(survey.getUtilities())
                .furniture(survey.getFurniture())
                .location(survey.getLocation())
                .build();
    }

    public SurveyResponse getSurveyById(Long id) {
        Optional<SurveyAnswer> survey = repository.findById(id);
        return survey.map(this::mapToResponse).orElse(null);
    }

    public boolean deleteSurveyById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public SurveyResponse getSurveyByCurrentUser() {
    String email = getCurrentUserEmail();
    Optional<SurveyAnswer> survey = repository.findByEmail(email);
    return survey.map(this::mapToResponse).orElse(null);
}

public SurveyResponse updateSurveyForCurrentUser(SurveyRequest request) {
    String email = getCurrentUserEmail();
    Optional<SurveyAnswer> optionalSurvey = repository.findByEmail(email);

    if (optionalSurvey.isEmpty()) return null;

    SurveyAnswer survey = optionalSurvey.get();

    // Cập nhật dữ liệu từ request
    survey.setUserName(request.getUserName());
    survey.setBirthYear(request.getBirthYear());
    survey.setHometown(request.getHometown());
    survey.setGender(request.getGender());
    survey.setOccupation(request.getOccupation());
    survey.setPriceRange(request.getPriceRange());
    survey.setCurrentLatitude(request.getCurrentLatitude());
    survey.setCurrentLongitude(request.getCurrentLongitude());
    survey.setPreferredLocation(request.getPreferredLocation());
    survey.setSmoking(request.getSmoking());
    survey.setPets(request.getPets());
    survey.setCookFrequency(request.getCookFrequency());
    survey.setSleepHabit(request.getSleepHabit());
    survey.setInviteFriends(request.getInviteFriends());
    survey.setPrice(request.getPrice());
    survey.setArea(request.getArea());
    survey.setGenderRequiment(request.getGenderRequiment());
    survey.setDeposit(request.getDeposit());
    survey.setUtilities(request.getUtilities());
    survey.setFurniture(request.getFurniture());
    survey.setLocation(request.getLocation());

    SurveyAnswer updated = repository.save(survey);
    return mapToResponse(updated);
}

private String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
}


}
