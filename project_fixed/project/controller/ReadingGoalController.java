package com.example.project.controller;

import com.example.project.dto.ReadingGoalDto;
import com.example.project.entity.ReadingGoal;
import com.example.project.service.ReadingGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class ReadingGoalController {

    private final ReadingGoalService goalService;

    // 목표 생성
    @PostMapping
    public ResponseEntity<ReadingGoalDto> createGoal(@RequestBody ReadingGoalDto dto) {
        return ResponseEntity.ok(goalService.createGoal(dto));
    }

    // 목표 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReadingGoalDto> getGoal(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.getGoal(id));
    }

    // 목표 수정
    @PutMapping("/{id}")
    public ResponseEntity<ReadingGoalDto> updateGoal(@PathVariable Long id, @RequestBody ReadingGoalDto dto) {
        return ResponseEntity.ok(goalService.updateGoal(id, dto));
    }

    // 목표 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

    // 책 한 권 완료
    @PostMapping("/{id}/complete-book")
    public ResponseEntity<ReadingGoalDto> completeBook(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.completeBook(id));
    }

    // 감상문 완료
    @PostMapping("/{id}/complete-review")
    public ResponseEntity<ReadingGoalDto> completeReview(@PathVariable Long id) {
        return ResponseEntity.ok(goalService.completeReview(id));
    }

    // 독서 시간 추가
    @PostMapping("/{id}/add-time")
    public ResponseEntity<ReadingGoalDto> addTime(@PathVariable Long id, @RequestBody AddTimeRequest request) {
        return ResponseEntity.ok(goalService.addReadingTime(id, request.getMinutes()));
    }

    // 요청 DTO
    @lombok.Data
    public static class AddTimeRequest {
        private int minutes;
    }
}
