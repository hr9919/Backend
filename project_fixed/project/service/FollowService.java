package com.example.project.service;

import com.example.project.dto.FollowDto;
import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowDto follow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();
        if (followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            return null; // already following
        }
        Follow f = followRepository.save(Follow.builder().follower(follower).following(following).build());
        return FollowDto.builder().followId(f.getFollowId()).followerId(followerId).followingId(followingId).build();
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById(followingId).orElseThrow();
        followRepository.findByFollowerAndFollowing(follower, following).ifPresent(followRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<Long> followers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return followRepository.findByFollowing(user).stream().map(f -> f.getFollower().getUserId()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Long> following(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return followRepository.findByFollower(user).stream().map(f -> f.getFollowing().getUserId()).collect(Collectors.toList());
    }
}
