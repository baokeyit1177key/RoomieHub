package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.SurveyRequest;
import org.example.roomiehub.dto.response.SurveyResponse;
import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.model.User;
import org.example.roomiehub.repository.SurveyRepository;
import org.example.roomiehub.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository repository;
    private final UserRepository userRepository;

    public SurveyResponse submitSurvey(SurveyRequest request) {
    // Lấy thông tin user đang login
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName(); // Lấy email/username

    // Tạo entity SurveyAnswer với userName = email
    SurveyAnswer survey = SurveyAnswer.builder()
            .userName(email)   // Gán email vào userName
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

    // Trả response
    return mapToResponse(savedSurvey);
}

    private SurveyResponse mapToResponse(SurveyAnswer survey) {
    return SurveyResponse.builder()
            .id(survey.getId())
            .userName(survey.getUserName())  // <--- Thêm dòng này!
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
}
