package org.example.roomiehub.repository;

import org.example.roomiehub.model.Payment;
import org.example.roomiehub.model.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    Optional<UserPackage> findFirstByUserIdAndActiveTrueOrderByEndDateDesc(Long userId);

}

