package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.exception.UserNotFoundException;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User loginOrRegister(String email, String nickname, String username, String provider) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.addSocialProvider(provider);
            return userRepository.save(user);
        } else {
            User newUser = new User(nickname, username, email);
            newUser.addSocialProvider(provider);
            return userRepository.save(newUser);
        }
    }

    // 전체 사용자 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ID로 사용자 조회 (Optional 반환)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ID로 사용자 조회 (없으면 예외 발생) → ReadingLogService에서 사용됨
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + id));
    }

    // 사용자 생성
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // 사용자 수정
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setNickname(updatedUser.getNickname());
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + id));
    }

    // 사용자 삭제
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + id);
        }
        userRepository.deleteById(id);
    }
}
