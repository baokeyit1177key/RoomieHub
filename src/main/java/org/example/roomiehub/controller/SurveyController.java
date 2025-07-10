package org.example.roomiehub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.roomiehub.dto.request.SurveyRequest;
import org.example.roomiehub.dto.response.SurveyResponse;
import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.service.impl.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping
public ResponseEntity<?> submitSurvey(@RequestBody SurveyRequest request) {
    SurveyResponse response = surveyService.submitSurvey(request);

    if (response == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                    "status", 409,
                    "error", "Survey already exists",
                    "message", "Bạn đã nộp khảo sát trước đó và không thể nộp lại."
                ));
    }

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

    // GET /api/surveys/me
@GetMapping("/me")
public ResponseEntity<?> getMySurvey() {
    SurveyResponse response = surveyService.getSurveyByCurrentUser();
    if (response == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Survey chưa tồn tại cho tài khoản hiện tại"));
    }
    return ResponseEntity.ok(response);
}

// PUT /api/surveys/me
@PutMapping("/me")
public ResponseEntity<?> updateMySurvey(@RequestBody SurveyRequest request) {
    SurveyResponse response = surveyService.updateSurveyForCurrentUser(request);
    if (response == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Survey chưa tồn tại. Vui lòng tạo trước."));
    }
    return ResponseEntity.ok(response);
}


}
