package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.BookRecommendDto;
import com.example.project.dto.GoalRecommendDto;
import com.example.project.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/recommend")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    // 책 추천 userId별 최신순 + 페이징
    @GetMapping("/books")
    public ResponseEntity<ApiResponse<Page<BookRecommendDto>>> getBookRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<BookRecommendDto> data = recommendService.getBookRecommendations(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    // 습관/목표 추천 userId별 가장 최신 1건
    @GetMapping("/goals")
    public ResponseEntity<ApiResponse<GoalRecommendDto>> getGoalRecommendation(
            @PathVariable Long userId
    ) {
        GoalRecommendDto data = recommendService.getLatestGoalRecommendation(userId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}