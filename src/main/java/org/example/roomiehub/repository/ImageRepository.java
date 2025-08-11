package org.example.roomiehub.repository;

;
import org.example.roomiehub.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
