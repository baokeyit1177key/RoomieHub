package org.example.roomiehub.repository;

import org.example.roomiehub.model.Payment;
import org.example.roomiehub.model.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    Optional<UserPackage> findFirstByUserIdAndActiveTrueOrderByEndDateDesc(Long userId);
    Optional<UserPackage> findByIdAndUserIdAndActiveTrue(Long id, Long userId);
    List<UserPackage> findByUserIdAndActiveTrue(Long userId);
    @Query("SELECT up FROM UserPackage up " +
            "WHERE up.user.id = :userId " +
            "AND up.active = true " +
            "AND up.endDate >= CURRENT_DATE")
    List<UserPackage> findActiveValidPackages(@Param("userId") Long userId);
    List<UserPackage> findByUserIdAndActiveTrueAndEndDateAfter(Long userId, LocalDate date);


}

