package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.enums.SocialLoginType;
import com.example.project.exception.UserNotFoundException;
import com.example.project.repository.FollowRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

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

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = findById(id);
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

        // 팔로우 관계 삭제
        followRepository.deleteByFollowerId(user.getId());
        followRepository.deleteByFollowingId(user.getId());

        // 유저 삭제
        userRepository.delete(user);
    }
}