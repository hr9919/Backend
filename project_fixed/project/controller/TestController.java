package com.example.project.controller;

import com.example.project.dto.LoginRequest;
import com.example.project.dto.LoginResponse;
import com.example.project.util.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class TestController {

    private final JwtUtil jwtUtil;

    public TestController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        if (request.getUsername().equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            return new LoginResponse(token);
        }
        throw new RuntimeException("Invalid credentials");
    }

    @GetMapping("/api/data")
    public String getData() {
        return "Protected Data Access Success!";
    }

    @GetMapping("/test/ping")
    public String ping() {
        return "pong";
    }
}
