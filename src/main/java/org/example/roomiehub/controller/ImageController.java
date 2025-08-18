package org.example.roomiehub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Operation(
            summary = "Upload multiple images and get Base64 strings",
            description = "Nhận nhiều ảnh multipart/form-data và trả về Base64 (chuỗi thô, không JSON bao ngoài)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Upload thành công")
            }
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(
            @Parameter(description = "Chọn nhiều file", required = true)
            @RequestPart("files") MultipartFile[] files) {

        StringBuilder sb = new StringBuilder();

        for (MultipartFile file : files) {
            try {
                String base64 = Base64.getEncoder().encodeToString(file.getBytes());
                sb.append(base64).append("\n"); // mỗi ảnh 1 dòng
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Failed to process file: " + file.getOriginalFilename());
            }
        }

        return ResponseEntity.ok(sb.toString().trim());
    }
}
