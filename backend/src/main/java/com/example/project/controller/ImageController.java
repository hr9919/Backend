package com.example.project.controller;

import com.example.project.service.ImageService;
import com.example.project.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/users/{userId}/profile-image")
    public ResponseEntity<ApiResponse<String>> uploadProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        String imageUrl = imageService.uploadProfileImage(userId, file);
        return ResponseEntity.ok(ApiResponse.success(imageUrl));
    }

    @PostMapping("/reviews/{reviewId}/images")
    public ResponseEntity<ApiResponse<List<String>>> uploadReviewImages(
            @PathVariable Long reviewId,
            @RequestParam("file") List<MultipartFile> files) {
        List<String> imageUrls = imageService.uploadReviewImages(reviewId, files);
        return ResponseEntity.ok(ApiResponse.success(imageUrls));
    }
}
