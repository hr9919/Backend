package com.example.project.controller;

import com.example.project.dto.ReadingLogDto;
import com.example.project.dto.ReadingLogStatsDto;
import com.example.project.entity.User;
import com.example.project.service.ReadingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class ReadingLogController {

    private final ReadingLogService logService;

    // 독서 기록 추가 or 수정
    @PostMapping
    public ReadingLogDto addOrUpdateLog(@RequestBody ReadingLogDto dto,
                                        @AuthenticationPrincipal User user) {
        return logService.addOrUpdateReadingLog(dto, user);
    }

    // 사용자별 독서 기록 조회
    @GetMapping
    public List<ReadingLogDto> getLogsByUser(@AuthenticationPrincipal User user) {
        return logService.getLogsByUser(user.getId()); // <-- 수정된 부분
    }

    // 사용자별 독서 통계 조회
    @GetMapping("/stats")
    public ReadingLogStatsDto getReadingStats(@AuthenticationPrincipal User user) {
        return logService.getReadingStats(user.getId()); // <-- 수정된 부분
    }
}
