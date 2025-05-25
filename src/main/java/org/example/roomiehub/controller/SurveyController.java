package org.example.roomiehub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.SurveyRequest;
import org.example.roomiehub.dto.response.SurveyResponse;
import org.example.roomiehub.service.impl.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
    public ResponseEntity<SurveyResponse> submitSurvey(@RequestBody SurveyRequest request) {
        SurveyResponse response = surveyService.submitSurvey(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Xem survey theo id
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(@PathVariable Long id) {
        SurveyResponse response = surveyService.getSurveyById(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    // Xóa survey theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurveyById(@PathVariable Long id) {
        boolean deleted = surveyService.deleteSurveyById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
