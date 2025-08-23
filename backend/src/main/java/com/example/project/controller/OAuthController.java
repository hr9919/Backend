package com.example.project.controller;

import com.example.project.dto.AuthResponse;
import com.example.project.enums.SocialLoginType;
import com.example.project.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;
    
    // Redirect URL 반환
    @GetMapping("/{socialLoginType}")
    public RedirectView redirect(@PathVariable SocialLoginType socialLoginType) {
        String redirectUrl = oAuthService.getRedirectUrl(socialLoginType);
        return new RedirectView(redirectUrl);
    }

    // Callback 처리 (JWT 발급 + 유저 정보 반환)
    @GetMapping("/{socialLoginType}/callback")
    public ResponseEntity<AuthResponse> callback(
            @PathVariable SocialLoginType socialLoginType,
            @RequestParam("code") String code
    ) {
        AuthResponse response = oAuthService.handleCallback(socialLoginType, code);
        return ResponseEntity.ok(response);
    }
}
