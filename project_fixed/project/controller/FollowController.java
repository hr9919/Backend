package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> follow(@RequestParam Long followerId,
                                                    @RequestParam Long followingId) {
        followService.follow(followerId, followingId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unfollow(@RequestParam Long followerId,
                                                      @RequestParam Long followingId) {
        followService.unfollow(followerId, followingId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<ApiResponse<List<Long>>> followers(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                followService.followers(userId)
        ));
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<ApiResponse<List<Long>>> following(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                followService.following(userId)
        ));
    }
}
