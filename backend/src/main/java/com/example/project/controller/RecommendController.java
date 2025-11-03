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

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<Page<BookRecommendDto>>> getBookRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Page<BookRecommendDto> data = recommendService.getBookRecommendations(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/goals")
    public ResponseEntity<ApiResponse<GoalRecommendDto>> getGoalRecommendation(
            @PathVariable Long userId
    ) {
        GoalRecommendDto data = recommendService.getLatestGoalRecommendation(userId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
