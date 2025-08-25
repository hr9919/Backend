package com.example.project.controller;

import com.example.project.dto.AuthResponse;
import com.example.project.dto.UserDto;
import com.example.project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public AuthResponse generateToken(@RequestBody TestRequest request) {
        Long userId = Long.parseLong(request.getUserId());
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        UserDto userDto = UserDto.builder().id(userId).build();

        return AuthResponse.builder()
                .user(userDto)
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        Long userId = jwtUtil.validateToken(token);
        return "UserId: " + userId;
    }
}

class TestRequest {
    private String userId;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
