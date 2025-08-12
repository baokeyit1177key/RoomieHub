package org.example.roomiehub.repository;

import org.example.roomiehub.model.ApartmentRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentRentalRepository extends JpaRepository<ApartmentRental, Long> {

    long countByUserId(Long userId);

    List<ApartmentRental> findByUserId(Long userId);

    void deleteById(Long id);

    /**
     * Tìm căn hộ theo tất cả tiêu chí (có lọc địa chỉ).
     */
    @Query("SELECT a FROM ApartmentRental a " +
           "WHERE a.price BETWEEN :minPrice AND :maxPrice " +
           "AND a.area BETWEEN :minArea AND :maxArea " +
           "AND (:gender IS NULL OR a.genderRequirement = :gender) " +
           "AND LOWER(a.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<ApartmentRental> findByAllCriteria(@Param("minPrice") double minPrice,
                                             @Param("maxPrice") double maxPrice,
                                             @Param("minArea") double minArea,
                                             @Param("maxArea") double maxArea,
                                             @Param("gender") String gender,
                                             @Param("address") String address);

    /**
     * Tìm căn hộ khi bỏ qua địa chỉ (chỉ lọc giá, diện tích, giới tính).
     */
    @Query("SELECT a FROM ApartmentRental a " +
           "WHERE a.price BETWEEN :minPrice AND :maxPrice " +
           "AND a.area BETWEEN :minArea AND :maxArea " +
           "AND (:gender IS NULL OR a.genderRequirement = :gender)")
    List<ApartmentRental> findByCriteriaWithoutAddress(@Param("minPrice") double minPrice,
                                                        @Param("maxPrice") double maxPrice,
                                                        @Param("minArea") double minArea,
                                                        @Param("maxArea") double maxArea,
                                                        @Param("gender") String gender);
}
