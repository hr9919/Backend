package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.request.AddExpRequest;
import com.example.project.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/level")
@RequiredArgsConstructor
public class LevelController {

    private final LevelService levelService;

    @PostMapping("/exp")
    public ResponseEntity<ApiResponse<Void>> addExperience(@RequestBody AddExpRequest request,
                                                           @RequestAttribute Long userId) {
        levelService.addExperience(userId, request.getPoints());
        return ResponseEntity.noContent().build();
    }
}