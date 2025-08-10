package org.example.roomiehub.repository;

import org.example.roomiehub.dto.response.UserInfoResponse;
import org.example.roomiehub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
@Query("SELECT COUNT(u) FROM User u")
     Long countTotalUsers();
    void deleteByEmail(String email);

    @Query("SELECT new org.example.roomiehub.dto.response.UserInfoResponse(u.id, u.fullName, u.email, u.role) FROM User u")
    List<UserInfoResponse> findAllUserInfo();
}
