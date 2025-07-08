package org.example.roomiehub.repository;

import org.example.roomiehub.model.RoommatePreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoommatePreferenceRepository extends JpaRepository<RoommatePreference, Long> {
    // Tìm tất cả RoommatePreference theo RoommatePost ID
    List<RoommatePreference> findByRoommatePostId(Long roommatePostId);
}