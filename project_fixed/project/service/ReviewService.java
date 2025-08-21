package com.example.project.service;

import com.example.project.dto.*;
import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReviewHashtagRepository reviewHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final FeedRepository feedRepository;

    @Transactional
    public ReviewDto create(CreateReviewRequest req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow();
        Book book = bookRepository.findById(req.getBookId()).orElseThrow();
        Review review = Review.builder()
                .content(req.getContent())
                .imageUrl(req.getImageUrl())
                .user(user)
                .book(book)
                .build();
        review = reviewRepository.save(review);
        // feed
        feedRepository.save(Feed.builder().user(user).review(review).build());

        // hashtags
        if (req.getHashtags() != null) {
            for (String tag : req.getHashtags()) {
                Hashtag h = hashtagRepository.findByTagText(tag)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagText(tag).build()));
                reviewHashtagRepository.save(ReviewHashtag.builder().hashtag(h).review(review).build());
            }
        }
        return toDto(review, user);
    }

    @Transactional
    public ReviewDto update(Long reviewId, UpdateReviewRequest req, Long me) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        if (!review.getUser().getUserId().equals(me)) throw new RuntimeException("FORBIDDEN");
        if (req.getContent() != null) review.setContent(req.getContent());
        if (req.getImageUrl() != null) review.setImageUrl(req.getImageUrl());

        // reset hashtags
        if (req.getHashtags() != null) {
            reviewHashtagRepository.deleteByReview(review);
            for (String tag : req.getHashtags()) {
                Hashtag h = hashtagRepository.findByTagText(tag)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagText(tag).build()));
                reviewHashtagRepository.save(ReviewHashtag.builder().hashtag(h).review(review).build());
            }
        }
        return toDto(review, review.getUser());
    }

    @Transactional
    public void delete(Long reviewId, Long me) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        if (!review.getUser().getUserId().equals(me)) throw new RuntimeException("FORBIDDEN");
        reviewHashtagRepository.deleteByReview(review);
        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public ReviewDto get(Long reviewId, Long me) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        return toDto(review, me != null ? userRepository.findById(me).orElse(null) : null);
    }

    private ReviewDto toDto(Review review, User meUser) {
        List<String> tags = reviewHashtagRepository.findByReview(review).stream()
                .map(rh -> rh.getHashtag().getTagText()).collect(Collectors.toList());
        long likeCount = reviewLikeRepository.countByReview(review);
        boolean likedByMe = false;
        if (meUser != null) {
            likedByMe = reviewLikeRepository.existsByUserAndReview(meUser, review);
        }
        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .imageUrl(review.getImageUrl())
                .userId(review.getUser().getUserId())
                .bookId(review.getBook().getBookId())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .hashtags(tags)
                .likeCount(likeCount)
                .likedByMe(likedByMe)
                .build();
    }
}
