package com.example.project.controller;

import com.example.project.dto.UserDto;
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

    @GetMapping("/{socialLoginType}")
    public RedirectView socialLogin(@PathVariable SocialLoginType socialLoginType) {
        String redirectUrl = oAuthService.getRedirectUrl(socialLoginType);
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/{socialLoginType}/callback")
    public ResponseEntity<UserDto> callback(@PathVariable SocialLoginType socialLoginType,
                                            @RequestParam String code) {
        return ResponseEntity.ok(oAuthService.handleCallback(socialLoginType, code));
    }
}
