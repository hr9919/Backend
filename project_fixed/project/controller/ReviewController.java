package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.CreateReviewRequest;
import com.example.project.dto.ReviewDto;
import com.example.project.dto.UpdateReviewRequest;
import com.example.project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> create(@RequestBody CreateReviewRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.create(req)
        ));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> update(@PathVariable Long reviewId,
                                                         @RequestParam Long me,
                                                         @RequestBody UpdateReviewRequest req) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.update(reviewId, me, req)
        ));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long reviewId,
                                                    @RequestParam Long me) {
        reviewService.delete(reviewId, me);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> get(@PathVariable Long reviewId,
                                                      @RequestParam(required = false) Long me) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.get(reviewId, me)
        ));
    }
}
