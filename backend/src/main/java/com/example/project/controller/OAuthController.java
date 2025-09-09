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

    @GetMapping("/{socialLoginType}")
    public RedirectView redirect(@PathVariable SocialLoginType socialLoginType) {
        String redirectUrl = oAuthService.getRedirectUrl(socialLoginType);
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/{socialLoginType}/callback")
    public RedirectView handleCallback(
            @PathVariable SocialLoginType socialLoginType,
            @RequestParam("code") String code
    ) {
        AuthResponse response = oAuthService.handleCallback(socialLoginType, code);
        
        String clientAppUrl = "meltingbooks://callback";
        String redirectUrl = UriComponentsBuilder.fromUriString(clientAppUrl)
                                .queryParam("token", response.getToken())
                                .queryParam("userId", response.getUser().getId())
                                .toUriString();
                                
        return new RedirectView(redirectUrl);
    }
}
