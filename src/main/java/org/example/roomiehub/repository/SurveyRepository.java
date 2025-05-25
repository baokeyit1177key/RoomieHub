package org.example.roomiehub.repository;


import org.example.roomiehub.model.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<SurveyAnswer, Long> {
}