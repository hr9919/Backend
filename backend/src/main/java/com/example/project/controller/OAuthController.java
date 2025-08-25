package com.example.project.controller;

import com.example.project.dto.AuthResponse;
import com.example.project.enums.SocialLoginType;
import com.example.project.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
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

    // Callback 처리 (JWT 발급 후 앱으로 리디렉션)
    @GetMapping("/{socialLoginType}/callback")
    public RedirectView handleCallback(
            @PathVariable SocialLoginType socialLoginType,
            @RequestParam("code") String code
    ) {
        AuthResponse response = oAuthService.handleCallback(socialLoginType, code);
        
        // 클라이언트 앱의 커스텀 URL 스킴으로 토큰과 유저 정보를 전달
        String clientAppUrl = "meltingbooks://callback";
        String redirectUrl = UriComponentsBuilder.fromUriString(clientAppUrl)
                                .queryParam("token", response.getToken())
                                .queryParam("userId", response.getUser().getId())
                                .toUriString();
                                
        return new RedirectView(redirectUrl);
    }
}
