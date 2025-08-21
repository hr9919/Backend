package com.example.project.service;

import com.example.project.dto.AuthResponse;
import com.example.project.dto.UserDto;
import com.example.project.entity.User;
import com.example.project.enums.SocialLoginType;
import com.example.project.repository.UserRepository;
import com.example.project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    public String getRedirectUrl(SocialLoginType socialLoginType) {
        if (socialLoginType == SocialLoginType.GOOGLE) {
            return "https://accounts.google.com/o/oauth2/v2/auth"
                    + "?client_id=" + googleClientId
                    + "&redirect_uri=" + googleRedirectUri
                    + "&response_type=code"
                    + "&scope=openid%20email%20profile";
        }
        throw new IllegalArgumentException("Unsupported social login type: " + socialLoginType);
    }

    public AuthResponse handleCallback(SocialLoginType socialLoginType, String code) {
        if (socialLoginType == SocialLoginType.GOOGLE) {
            return handleGoogleCallback(code);
        }
        throw new IllegalArgumentException("Unsupported social login type: " + socialLoginType);
    }

    private AuthResponse handleGoogleCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 구글 OAuth 토큰 요청
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("code", code);
        tokenRequest.put("client_id", googleClientId);
        tokenRequest.put("client_secret", googleClientSecret);
        tokenRequest.put("redirect_uri", googleRedirectUri);
        tokenRequest.put("grant_type", "authorization_code");

        Map<String, Object> tokenResponse = restTemplate.postForObject(
                "https://oauth2.googleapis.com/token", tokenRequest, Map.class);

        String accessToken = (String) tokenResponse.get("access_token");

        // 2. 사용자 정보 요청
        Map<String, Object> userInfo = restTemplate.getForObject(
                "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken,
                Map.class
        );

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String picture = (String) userInfo.get("picture");
        String googleId = (String) userInfo.get("id");

        // 3. 사용자 조회/등록
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);

            String baseUsername = email.split("@")[0];
            String username = baseUsername;
            int count = 1;
            while (userRepository.findByUsername(username).isPresent()) {
                username = baseUsername + "_" + count++;
            }
            user.setUsername(username);

            String baseNickname = (name != null ? name : baseUsername);
            String nickname = baseNickname;
            count = 1;
            while (userRepository.findByNickname(nickname).isPresent()) {
                nickname = baseNickname + "_" + count++;
            }
            user.setNickname(nickname);

            user.setSocialProviders(new ArrayList<>());
        }

        user.setProfileImage(picture);
        user.setGoogleId(googleId);
        user.setLoginType(SocialLoginType.GOOGLE);

        if (!user.getSocialProviders().contains("GOOGLE")) {
            user.getSocialProviders().add("GOOGLE");
        }

        user = userRepository.save(user);

        // 4. JWT 발급
        String jwt = jwtUtil.generateToken(user.getId());

        // 5. UserDto + JWT 반환
        return AuthResponse.builder()
                .user(UserDto.from(user))
                .token(jwt)
                .build();
    }
}