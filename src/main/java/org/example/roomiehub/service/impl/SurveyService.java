package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.SurveyRequest;
import org.example.roomiehub.dto.response.SurveyResponse;
import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.repository.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository repository;

    // Nhận DTO request, trả DTO response
    public SurveyResponse submitSurvey(SurveyRequest request) {
        // Chuyển từ DTO request sang Entity
        SurveyAnswer survey = SurveyAnswer.builder()
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
                .build();

        // Lưu entity vào DB
        SurveyAnswer savedSurvey = repository.save(survey);

        // Chuyển từ Entity sang DTO response để trả về
        return mapToResponse(savedSurvey);
    }

    // Helper chuyển Entity -> Response DTO
    private SurveyResponse mapToResponse(SurveyAnswer survey) {
        return SurveyResponse.builder()
                .id(survey.getId())
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
                .build();
    }

    public SurveyResponse getSurveyById(Long id) {
    Optional <SurveyAnswer> survey = repository.findById(id);
    return survey.map(this::mapToResponse).orElse(null);
}

public boolean deleteSurveyById(Long id) {
    if (repository.existsById(id)) {
        repository.deleteById(id);
        return true;
    }
    return false;
}
}
