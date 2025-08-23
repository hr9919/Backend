package com.example.project.service;

import com.example.project.entity.Badge;
import com.example.project.entity.User;
import com.example.project.repository.BadgeRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BadgeService {
    
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void checkAndAwardBadge(String badgeName, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Badge> existingBadge = badgeRepository.findByUserAndBadgeName(user, badgeName);
        if (existingBadge.isEmpty()) {
            // 배지 이름에 따라 이미지 URL 설정
            String imageUrl = getBadgeImageUrl(badgeName, "브론즈"); // 티어 정보도 함께 전달 가능
            
            Badge badge = Badge.builder()
                    .user(user)
                    .badgeName(badgeName)
                    .tier("브론즈")
                    .imageUrl(imageUrl) // 이미지 URL 추가
                    .build();
            badgeRepository.save(badge);
        }
    }

    // 배지 이름과 티어에 따라 이미지 URL을 반환하는 메서드
    private String getBadgeImageUrl(String badgeName, String tier) {
        // 실제 배지 이미지가 저장된 경로를 사용하세요.
        // 예를 들어 S3 버킷이나, 서버의 정적 파일 경로 등
        // 이 로직은 백엔드에서 관리하는 것이 좋습니다.
        if ("첫 완독 배지".equals(badgeName)) {
            return "https://your-domain.com/badges/first-read-bronze.png";
        }
        if ("감상문 마스터 배지".equals(badgeName)) {
            return "https://your-domain.com/badges/review-master-bronze.png";
        }
        return "https://your-domain.com/badges/default.png";
    }
    
    @Transactional
    public void checkFirstBookBadge(Long userId) {
        checkAndAwardBadge("첫 완독 배지", userId);
    }
    
    @Transactional
    public void checkMasterBadge(Long userId) {
        checkAndAwardBadge("감상문 마스터 배지", userId);
    }
}
