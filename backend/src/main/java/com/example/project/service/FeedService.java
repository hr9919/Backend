package com.example.project.service;

import com.example.project.dto.FeedDto;
import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.repository.FollowRepository;
import com.example.project.repository.ReviewRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public List<FeedDto> getFeed(Long userId, int page, int size) {

        // 1. 팔로잉한 유저 ID 리스트 + 내 ID 포함
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
        followingIds.add(userId); // 내 글도 포함

        // 2. 팔로우 + 내 리뷰 최신순 조회
        List<Review> reviews = reviewRepository.findByUser_IdInOrderByCreatedAtDesc(followingIds, PageRequest.of(page, size));

        // 3. FeedDto 변환
        List<FeedDto> feedList = new ArrayList<>();
        for (Review r : reviews) {
            FeedDto dto = FeedDto.builder()
                    .reviewId(r.getId())
                    .userId(r.getUser().getId())
                    .username(r.getUser().getUsername())
                    .tagId(r.getUser().getTagId())
                    .userProfileImage(r.getUser().getProfileImageUrl()) // 프로필 이미지
                    .content(r.getContent())
                    .rating(r.getRating())
                    .reviewImageUrls(r.getReviewImageUrls())
                    .createdAt(r.getCreatedAt())
                    .likeCount(r.getLikes() != null ? r.getLikes().size() : 0)
                    .commentCount(r.getComments() != null ? r.getComments().size() : 0)
                    .hashtags(r.getHashtags() != null
    ? r.getHashtags().stream()
          .map(rh -> rh.getHashtag().getTagText())
          .collect(Collectors.toList())
    : new ArrayList<>())
                    .bookId(r.getBook().getId())
                    .bookTitle(r.getBook().getTitle())
                    .build();
            feedList.add(dto);
        }

        return feedList;
    }
}
