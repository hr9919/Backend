package com.example.project.service;

import com.example.project.dto.UserDto;
import com.example.project.entity.User;
import com.example.project.enums.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserService userService;

    // 소셜 로그인 페이지 URL 반환
    public String getRedirectUrl(SocialLoginType type) {
        switch (type) {
            case GOOGLE:
                return "https://accounts.google.com/o/oauth2/auth?...";
            case KAKAO:
                return "https://kauth.kakao.com/oauth/authorize?...";
            case NAVER:
                return "https://nid.naver.com/oauth2.0/authorize?...";
            default:
                throw new IllegalArgumentException("지원하지 않는 소셜 로그인 타입: " + type);
        }
    }

    // 콜백 처리 (테스트용 단순 구현)
    public UserDto handleCallback(SocialLoginType type, String code) {
        // 테스트용 하드코딩 데이터
        String email = type.toString().toLowerCase() + "_user@example.com";
        String nickname = type.toString() + "_nickname";
        String username = type.toString() + "_username";

        User user = userService.loginOrRegister(email, nickname, username, type);
        return UserDto.fromEntity(user);
    }
}
