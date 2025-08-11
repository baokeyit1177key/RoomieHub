package org.example.roomiehub.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.roomiehub.model.ImageEntity;
import org.example.roomiehub.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageEntity saveImage(MultipartFile file) throws Exception {
        return imageRepository.save(ImageEntity.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .data(file.getBytes())
                .build());
    }

    public ImageEntity getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}
