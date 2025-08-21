package com.example.project.controller;

import com.example.project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public String generateToken(@RequestBody TestRequest request) {
        Long userId = Long.parseLong(request.getId());
        return jwtUtil.generateToken(userId);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        Long userId = jwtUtil.validateToken(token);
        return "UserId: " + userId;
    }
}

class TestRequest {
    private String userId;

    public String getId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
