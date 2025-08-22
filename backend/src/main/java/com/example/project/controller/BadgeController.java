package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    // 첫 완독 배지 지급 API
    @PostMapping("/check-first-book")
    public ResponseEntity<ApiResponse<Void>> checkFirstBookBadge(@RequestAttribute Long userId) {
        badgeService.checkFirstBookBadge(userId);
        return ResponseEntity.noContent().build();
    }
    
    // 감상문 마스터 배지 지급 API
    @PostMapping("/check-master-badge")
    public ResponseEntity<ApiResponse<Void>> checkMasterBadge(@RequestAttribute Long userId) {
        badgeService.checkMasterBadge(userId);
        return ResponseEntity.noContent().build();
    }
}
