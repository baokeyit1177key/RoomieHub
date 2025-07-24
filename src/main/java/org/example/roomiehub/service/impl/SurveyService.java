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
    String email = getCurrentUserEmail();

    if (repository.findByEmail(email).isPresent()) return null;

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

    return mapToResponse(repository.save(survey));
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

    if (request.getUserName() != null) survey.setUserName(request.getUserName());
    if (request.getBirthYear() != null) survey.setBirthYear(request.getBirthYear());
    if (request.getHometown() != null) survey.setHometown(request.getHometown());
    if (request.getGender() != null) survey.setGender(request.getGender());
    if (request.getOccupation() != null) survey.setOccupation(request.getOccupation());
    if (request.getPriceRange() != null) survey.setPriceRange(request.getPriceRange());
    if (request.getCurrentLatitude() != null) survey.setCurrentLatitude(request.getCurrentLatitude());
    if (request.getCurrentLongitude() != null) survey.setCurrentLongitude(request.getCurrentLongitude());
    if (request.getPreferredLocation() != null) survey.setPreferredLocation(request.getPreferredLocation());
    if (request.getSmoking() != null) survey.setSmoking(request.getSmoking());
    if (request.getPets() != null) survey.setPets(request.getPets());
    if (request.getCookFrequency() != null) survey.setCookFrequency(request.getCookFrequency());
    if (request.getSleepHabit() != null) survey.setSleepHabit(request.getSleepHabit());
    if (request.getInviteFriends() != null) survey.setInviteFriends(request.getInviteFriends());
    if (request.getPrice() != null) survey.setPrice(request.getPrice());
    if (request.getArea() != null) survey.setArea(request.getArea());
    if (request.getGenderRequiment() != null) survey.setGenderRequiment(request.getGenderRequiment());
    if (request.getDeposit() != null) survey.setDeposit(request.getDeposit());
    if (request.getUtilities() != null) survey.setUtilities(request.getUtilities());
    if (request.getFurniture() != null) survey.setFurniture(request.getFurniture());
    if (request.getLocation() != null) survey.setLocation(request.getLocation());

    return mapToResponse(repository.save(survey));
}

private String getCurrentUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
}


}
