package com.example.project.service;

import com.example.project.dto.AuthResponse;
import com.example.project.dto.UserDto;
import com.example.project.enums.SocialLoginType;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;
    
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;


    public String getRedirectUrl(SocialLoginType socialLoginType) {
        switch (socialLoginType) {
            case GOOGLE:
                return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                        .queryParam("client_id", googleClientId)
                        .queryParam("redirect_uri", googleRedirectUri)
                        .queryParam("response_type", "code")
                        .queryParam("scope", "openid email profile")
                        .toUriString();
            case NAVER:
                return UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                        .queryParam("client_id", naverClientId)
                        .queryParam("redirect_uri", naverRedirectUri)
                        .queryParam("response_type", "code")
                        .toUriString();
            case KAKAO:
                return UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                        .queryParam("client_id", kakaoClientId)
                        .queryParam("redirect_uri", kakaoRedirectUri)
                        .queryParam("response_type", "code")
                        .toUriString();
            default:
                throw new IllegalArgumentException("Unsupported social login type: " + socialLoginType);
        }
    }

    public AuthResponse handleCallback(SocialLoginType socialLoginType, String code) {
        switch (socialLoginType) {
            case GOOGLE:
                return handleGoogleCallback(code);
            case NAVER:
                return handleNaverCallback(code);
            case KAKAO:
                return handleKakaoCallback(code);
            default:
                throw new IllegalArgumentException("Unsupported social login type: " + socialLoginType);
        }
    }

    private AuthResponse handleGoogleCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("code", code);
        tokenRequest.put("client_id", googleClientId);
        tokenRequest.put("client_secret", googleClientSecret);
        tokenRequest.put("redirect_uri", googleRedirectUri);
        tokenRequest.put("grant_type", "authorization_code");

        Map<String, Object> tokenResponse = restTemplate.postForObject(
                "https://oauth2.googleapis.com/token", tokenRequest, Map.class);
        
        String accessToken = (String) tokenResponse.get("access_token");

        Map<String, Object> userInfo = restTemplate.getForObject(
                "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken,
                Map.class
        );

        String email = (String) userInfo.get("email");
        String nickname = (String) userInfo.get("name");
        String profileImage = (String) userInfo.get("picture");
        String googleId = (String) userInfo.get("id");

        User user = loginOrRegister(email, nickname, email.split("@")[0], profileImage, googleId, SocialLoginType.GOOGLE);
        String jwt = jwtUtil.generateAccessToken(user.getId());
        user.setAccessToken(jwt);
        userRepository.save(user);

        return AuthResponse.builder().user(UserDto.from(user)).token(jwt).build();
    }

    private AuthResponse handleNaverCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String tokenUri = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverClientId)
                .queryParam("client_secret", naverClientSecret)
                .queryParam("redirect_uri", naverRedirectUri)
                .queryParam("code", code)
                .toUriString();
        
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUri, HttpMethod.POST, null, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me", HttpMethod.GET, entity, Map.class);
        Map<String, Object> userInfo = (Map<String, Object>) userInfoResponse.getBody().get("response");
        
        String naverId = (String) userInfo.get("id");
        String email = (String) userInfo.get("email");
        String nickname = (String) userInfo.get("nickname");
        String profileImage = (String) userInfo.get("profile_image");

        User user = loginOrRegister(email, nickname, email.split("@")[0], profileImage, naverId, SocialLoginType.NAVER);
        String jwt = jwtUtil.generateAccessToken(user.getId());
        user.setAccessToken(jwt);
        userRepository.save(user);

        return AuthResponse.builder().user(UserDto.from(user)).token(jwt).build();
    }

    private AuthResponse handleKakaoCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String tokenUri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", kakaoClientId)
                .queryParam("client_secret", kakaoClientSecret)
                .queryParam("redirect_uri", kakaoRedirectUri)
                .queryParam("code", code)
                .toUriString();
        
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUri, HttpMethod.POST, null, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, Map.class);
        Map<String, Object> userInfo = (Map<String, Object>) userInfoResponse.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoId = userInfo.get("id").toString();
        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        User user = loginOrRegister(email, nickname, email.split("@")[0], profileImage, kakaoId, SocialLoginType.KAKAO);
        String jwt = jwtUtil.generateAccessToken(user.getId());
        user.setAccessToken(jwt);
        userRepository.save(user);

        return AuthResponse.builder().user(UserDto.from(user)).token(jwt).build();
    }

    private User loginOrRegister(String email, String nickname, String username, String profileImage, String socialId, SocialLoginType type) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user.setUsername(username);
            user.setProfileImageUrl(profileImage);
            user.setSocialProviders(new ArrayList<>());
        }
        
        user.setLoginType(type);
        user.addSocialProvider(type.name());
        
        switch(type) {
            case GOOGLE:
                user.setGoogleId(socialId);
                break;
            case NAVER:
                user.setNaverId(socialId);
                break;
            case KAKAO:
                user.setKakaoId(socialId);
                break;
        }

        return userRepository.save(user);
    }
}
