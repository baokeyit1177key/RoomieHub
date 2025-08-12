package org.example.roomiehub.repository;

import org.example.roomiehub.Enum.Enums;
import org.example.roomiehub.Enum.GenderLevel;
import org.example.roomiehub.model.RoommatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoommatePostRepository extends JpaRepository<RoommatePost, Long> {

    // Lấy danh sách post theo email người sở hữu
    List<RoommatePost> findByOwnerPost(String ownerPost);

     @Query("SELECT DISTINCT p FROM RoommatePost p JOIN p.roommatePreferences pref " +
           "WHERE pref.gender = :gender " +
           "AND p.monthlyRentPrice BETWEEN :minPrice AND :maxPrice " +
           "AND (:kw1 IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :kw1, '%'))) " +
           "AND (:kw2 IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :kw2, '%'))) " +
           "AND (:kw3 IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :kw3, '%')))")
    List<RoommatePost> findByGenderPriceAndAddress(
            @Param("gender") Enums.Gender gender,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice,
            @Param("kw1") String kw1,
            @Param("kw2") String kw2,
            @Param("kw3") String kw3
    );

     @Query("SELECT p FROM RoommatePost p JOIN p.roommatePreferences pref " +
       "WHERE pref.gender = :gender " +
       "AND p.monthlyRentPrice BETWEEN :minPrice AND :maxPrice")
List<RoommatePost> findByGenderAndPriceRange(@Param("gender") Enums.Gender gender,
                                             @Param("minPrice") double minPrice,
                                             @Param("maxPrice") double maxPrice);

}
