package org.example.roomiehub.repository;

import org.example.roomiehub.model.ApartmentRental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApartmentRentalRepository extends JpaRepository<ApartmentRental, Long> {

}
