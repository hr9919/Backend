package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ReadingLogDto;
import com.example.project.dto.ReadingLogStatsDto;
import com.example.project.service.ReadingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/logs")
@RequiredArgsConstructor
public class ReadingLogController {

    private final ReadingLogService logService;

    // 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ReadingLogDto>> createLog(
            @PathVariable Long userId,
            @RequestBody ReadingLogDto dto) {
        return ResponseEntity.ok(ApiResponse.success(logService.create(userId, dto)));
    }

    // 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadingLogDto>>> getLogs(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(logService.getByUser(userId)));
    }

    // 수정
    @PutMapping("/{logId}")
    public ResponseEntity<ApiResponse<ReadingLogDto>> updateLog(
            @PathVariable Long userId,
            @PathVariable Long logId,
            @RequestBody ReadingLogDto dto) {
        return ResponseEntity.ok(ApiResponse.success(logService.update(userId, logId, dto)));
    }

    // 삭제
    @DeleteMapping("/{logId}")
    public ResponseEntity<ApiResponse<Void>> deleteLog(
            @PathVariable Long userId,
            @PathVariable Long logId) {
        logService.delete(userId, logId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 통계
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<ReadingLogStatsDto>> getStats(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(logService.getStats(userId)));
    }
}