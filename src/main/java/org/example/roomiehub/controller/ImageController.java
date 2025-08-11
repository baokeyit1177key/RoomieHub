package org.example.roomiehub.controller;

import lombok.RequiredArgsConstructor;
import org.example.roomiehub.model.ImageEntity;
import org.example.roomiehub.service.impl.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

 @PostMapping(value = "/upload", consumes = "multipart/form-data")
public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
    ImageEntity saved = imageService.saveImage(file);
    return ResponseEntity.ok("Image saved with ID: " + saved.getId());
}
    // Lấy ảnh
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        ImageEntity image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
                .body(image.getData());
    }
}
