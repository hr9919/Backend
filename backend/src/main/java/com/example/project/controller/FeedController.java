package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.FeedDto;
import com.example.project.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedDto>>> getFeed(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<FeedDto> feedList = feedService.getFeed(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(feedList));
    }
}
