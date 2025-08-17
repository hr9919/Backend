package com.example.project.controller;

import com.example.project.dto.UserDto;
import com.example.project.enums.SocialLoginType;
import com.example.project.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{socialLoginType}")
    public ResponseEntity<String> socialLogin(@PathVariable SocialLoginType socialLoginType) {
        return ResponseEntity.ok(oAuthService.getRedirectUrl(socialLoginType));
    }

    @GetMapping("/{socialLoginType}/callback")
    public ResponseEntity<UserDto> callback(@PathVariable SocialLoginType socialLoginType,
                                            @RequestParam String code) {
        return ResponseEntity.ok(oAuthService.handleCallback(socialLoginType, code));
    }
}
