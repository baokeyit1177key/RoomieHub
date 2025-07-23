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

}
