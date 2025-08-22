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
            Badge badge = Badge.builder()
                    .user(user)
                    .badgeName(badgeName)
                    .tier("브론즈")
                    .build();
            badgeRepository.save(badge);
        }
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