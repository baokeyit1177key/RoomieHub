package org.example.roomiehub.repository;

import org.example.roomiehub.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderCode(Long orderCode);
      List<Payment> findByStatus(String status);
      // PaymentRepository.java
@Query("SELECT COUNT(DISTINCT p.email) FROM Payment p WHERE p.status = :status")
Long countDistinctEmailByStatus(@Param("status") String status);

}
