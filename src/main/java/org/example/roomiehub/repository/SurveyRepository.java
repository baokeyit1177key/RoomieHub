package org.example.roomiehub.repository;


import org.example.roomiehub.model.SurveyAnswer;
import org.example.roomiehub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<SurveyAnswer, Long> {

    Optional<SurveyAnswer> findByEmail(String email);

}