package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.FeedDto;
import com.example.project.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FeedDto>>> getFeed(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Long viewerId = null;
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            viewerId = (Long) authentication.getPrincipal();
        }

        Page<FeedDto> feedPage = feedService.getFeed(viewerId, page, size);
        return ResponseEntity.ok(ApiResponse.success(feedPage));
    }
}
