package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> get(@PathVariable Long userId) {
        User u = userRepository.findById(userId).orElseThrow();
        Map<String, Object> body = new HashMap<>();
        body.put("userId", u.getUserId());
        body.put("email", u.getEmail());
        body.put("nickname", u.getNickname());
        body.put("tagId", u.getTagId());
        body.put("profileImage", u.getProfileImage());
        body.put("bio", u.getBio());
        body.put("createdAt", u.getCreatedAt());
        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long userId,
                                                    @RequestBody UpdateProfileRequest req) {
        User u = userRepository.findById(userId).orElseThrow();
        safeSet(u, "nickname", req.getNickname());
        safeSet(u, "tagId", req.getTagId());
        safeSet(u, "bio", req.getBio());
        safeSet(u, "profileImage", req.getProfileImage());
        userRepository.save(u);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private Object safeGet(User u, String field) {
        Field f = ReflectionUtils.findField(User.class, field);
        if (f == null) return null;
        f.setAccessible(true);
        try { return f.get(u); } catch (IllegalAccessException e) { return null; }
    }

    private void safeSet(User u, String field, Object value) {
        if (value == null) return;
        Field f = ReflectionUtils.findField(User.class, field);
        if (f == null) return;
        f.setAccessible(true);
        try { f.set(u, value); } catch (IllegalAccessException ignored) {}
    }

    @Data
    public static class UpdateProfileRequest {
        private String nickname;
        private String tagId;
        private String bio;
        private String profileImage;
    }
}
