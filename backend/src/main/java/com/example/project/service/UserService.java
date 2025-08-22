package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.enums.SocialLoginType;
import com.example.project.exception.UserNotFoundException;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


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
        return userRepository.findByEmail(email)
                .map(user -> {
                    if (!user.getSocialProviders().contains(type.name())) {
                        user.getSocialProviders().add(type.name());
                    }
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .nickname(nickname)
                            .username(username)
                            .build();
                    newUser.getSocialProviders().add(type.name());
                    return userRepository.save(newUser);
                });
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
        // tagId 중복 검사 추가
        if (user.getTagId() != null && userRepository.findByTagId(user.getTagId()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 tagId입니다.");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = findById(id);

        // tagId가 변경되었을 경우, 변경된 tagId가 이미 존재하는지 검사
        if (updatedUser.getTagId() != null && !updatedUser.getTagId().equals(user.getTagId())) {
            if (userRepository.findByTagId(updatedUser.getTagId()).isPresent()) {
                throw new RuntimeException("이미 사용 중인 tagId입니다.");
            }
        }

        user.setEmail(updatedUser.getEmail());
        user.setNickname(updatedUser.getNickname());
        user.setUsername(updatedUser.getUsername());
        user.setProfileImage(updatedUser.getProfileImage());
        user.setBio(updatedUser.getBio());
        user.setTagId(updatedUser.getTagId());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + id));
        followRepository.deleteByFollower(user);
        followRepository.deleteByFollowing(user);
        readingLogRepository.deleteByUser(user);
        reviewRepository.deleteByUser(user);
        readingGoalRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}