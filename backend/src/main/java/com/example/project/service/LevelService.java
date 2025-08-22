package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LevelService {

    private final UserRepository userRepository;

    private static final int BASE_EXP_LEVEL_2 = 300;
    private static final int TIER_1_LEVEL_END = 49;
    private static final double TIER_1_MULTIPLIER = 10.0;
    private static final double TIER_2_MULTIPLIER = 30.0;

    @Transactional
    public void addExperience(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setExperience(user.getExperience() + points);
        
        checkLevelUp(user);
        userRepository.save(user);
    }

    private void checkLevelUp(User user) {
        int currentLevel = user.getLevel();
        int currentExp = user.getExperience();
        
        while (currentExp >= getRequiredExpForNextLevel(currentLevel)) {
            user.setLevel(currentLevel + 1);
            user.setExperience(currentExp - getRequiredExpForNextLevel(currentLevel));
            currentLevel = user.getLevel();
            currentExp = user.getExperience();
        }
    }

    public int getRequiredExpForNextLevel(int currentLevel) {
        if (currentLevel == 1) {
            return BASE_EXP_LEVEL_2;
        } else if (currentLevel < TIER_1_LEVEL_END) {
            return (int) (TIER_1_MULTIPLIER * Math.pow(currentLevel - 1, 1.2));
        } else { // 50레벨 이상
            return (int) (TIER_2_MULTIPLIER * Math.pow(currentLevel - 1, 1.2));
        }
    }
}