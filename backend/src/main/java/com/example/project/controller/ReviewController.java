package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ReviewDto;
import com.example.project.dto.request.CreateReviewRequest;
import com.example.project.dto.request.UpdateReviewRequest;
import com.example.project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> create(
            @RequestBody CreateReviewRequest req,
            @RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.create(req, userId)));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> update(
            @PathVariable Long reviewId,
            @RequestParam Long userId,
            @RequestBody UpdateReviewRequest req) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.update(reviewId, userId, req)));
    }

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> get(
            @PathVariable Long reviewId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.get(reviewId, userId)));
    }

    // 전체 리뷰 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAll()));
    }

    // 특정 유저의 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getByUser(userId)));
    }

    // 특정 책의 리뷰 조회
    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getByBook(bookId)));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long reviewId,
            @RequestParam Long userId) {
        reviewService.delete(reviewId, userId);
        return ResponseEntity.noContent().build();
    }
}