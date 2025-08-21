package com.example.project.service;

import com.example.project.dto.*;
import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ReviewService reviewService;

    @Transactional(readOnly = true)
    public List<FeedDto> getFeedForUser(Long userId, int page, int size) {
        User me = userRepository.findById(userId).orElseThrow();
        List<User> following = followRepository.findByFollower(me).stream()
                .map(Follow::getFollowing).collect(Collectors.toList());
        following.add(me); // include myself
        List<Feed> feeds = feedRepository.findByUserInOrderByCreatedAtDesc(following, PageRequest.of(page, size));
        return feeds.stream().map(f -> FeedDto.builder()
                .feedId(f.getFeedId())
                .reviewId(f.getReview().getReviewId())
                .userId(f.getUser().getUserId())
                .createdAt(f.getCreatedAt())
                .review(reviewService.get(f.getReview().getReviewId(), userId))
                .build()).collect(Collectors.toList());
    }
}
