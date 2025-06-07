package org.example.roomiehub.repository;

import org.example.roomiehub.model.ApartmentRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentRentalRecommentRepository extends JpaRepository<ApartmentRental, Long> {

    @Query(value = """
        SELECT a.* FROM apartment_rental a
        WHERE 
            ABS(a.price - :price) BETWEEN 1000000 AND 2000000
            AND ABS(a.area - :area) BETWEEN 10 AND 20
            AND a.gender_requirement = :genderRequirement
            AND ABS(a.deposit - :deposit) BETWEEN 1000000 AND 2000000
            AND a.utilities = :utilities
            AND a.furniture = :furniture
            AND (
                6371 * acos(
                    cos(radians(:latitude)) * cos(radians(a.latitude)) *
                    cos(radians(a.longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) * sin(radians(a.latitude))
                )
            ) <= 5
        """,
        nativeQuery = true)
    List<ApartmentRental> findSuitableApartments(
            @Param("price") double price,
            @Param("area") double area,
            @Param("genderRequirement") String genderRequirement,
            @Param("deposit") double deposit,
            @Param("utilities") String utilities,
            @Param("furniture") String furniture,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude
    );

    @Query(value = """
        SELECT a.* FROM apartment_rental a
        WHERE ABS(a.price - :price) <= 3000000
        ORDER BY ABS(a.price - :price)
        LIMIT 10
        """,
        nativeQuery = true)
    List<ApartmentRental> findRandomByPriceClose(
            @Param("price") double price
    );
}
