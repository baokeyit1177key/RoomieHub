package org.example.roomiehub.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.model.ImageEntity;
import org.example.roomiehub.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    // Lưu ảnh upload từ client
    public ImageEntity saveImage(MultipartFile file) throws Exception {
        return imageRepository.save(ImageEntity.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .data(file.getBytes())
                .build());
    }

    // Lấy ảnh từ DB
    public ImageEntity getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    // Convert ảnh URL thành Base64 (không lưu DB)
    public String convertUrlToBase64(String imageUrl) throws Exception {
        try (InputStream in = new URL(imageUrl).openStream()) {
            byte[] bytes = in.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

    // Convert list URL → list base64
    public List<String> convertUrlListToBase64(List<String> urls) {
        return urls.stream().map(url -> {
            try {
                return convertUrlToBase64(url);
            } catch (Exception e) {
                return null;
            }
        }).collect(Collectors.toList());
    }
}
