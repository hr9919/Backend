package com.example.project.service;

import com.example.project.dto.UserDto;
import com.example.project.entity.User;
import com.example.project.enums.SocialLoginType;
import com.example.project.exception.UserNotFoundException;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ReadingLogRepository readingLogRepository;
    private final ReviewRepository reviewRepository;
    private final ReadingGoalRepository readingGoalRepository;

    @Transactional
    public User loginOrRegister(String email, String nickname, String username, SocialLoginType type) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .username(username)
                            .build();
                    newUser.getSocialProviders().add(type.name());
                    return userRepository.save(newUser);
                });

        if (!user.getSocialProviders().contains(type.name())) {
            user.getSocialProviders().add(type.name());
            userRepository.save(user);
        }
        
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + id));
    }

    @Transactional
    public User createUser(User user) {
        if (user.getTagId() != null && userRepository.findByTagId(user.getTagId()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 tagId입니다.");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = findById(id);
        
        if (updatedUser.getTagId() != null && !updatedUser.getTagId().equals(user.getTagId())) {
            if (userRepository.findByTagId(updatedUser.getTagId()).isPresent()) {
                throw new RuntimeException("이미 사용 중인 tagId입니다.");
            }
        }
        
        user.setEmail(updatedUser.getEmail());
        user.setNickname(updatedUser.getNickname());
        user.setUsername(updatedUser.getUsername());
        user.setProfileImageUrl(updatedUser.getProfileImageUrl());
        user.setBio(updatedUser.getBio());
        user.setTagId(updatedUser.getTagId());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        followRepository.deleteByFollower(user);
        followRepository.deleteByFollowing(user);
        readingLogRepository.deleteByUser(user);
        reviewRepository.deleteByUser(user);
        readingGoalRepository.deleteByUser(user);
        userRepository.delete(user);
    }
    
    // 인기 유저 추천 로직 (팔로워 수 + 리뷰 수 기준)
    public List<UserDto> getPopularUsers() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparingInt((User u) -> u.getFollowers().size() + u.getReviews().size()).reversed()) // ✅ User 타입 명시
                .map(UserDto::from)
                .collect(Collectors.toList());
    }
}
