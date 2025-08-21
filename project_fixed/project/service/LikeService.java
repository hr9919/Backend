package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final NotificationService notificationService;
    private final ReviewLikeRepository reviewLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likeReview(Long reviewId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        if (!reviewLikeRepository.existsByUserAndReview(user, review)) {
            reviewLikeRepository.save(ReviewLike.builder().user(user).review(review).build());
            notificationService.pushToUser(review.getUser().getUserId(), "새 좋아요", "내 감상문에 좋아요가 달렸어요");
        }
    }

    @Transactional
    public void unlikeReview(Long reviewId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        reviewLikeRepository.findByUserAndReview(user, review).ifPresent(reviewLikeRepository::delete);
    }

    @Transactional
    public void likeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!commentLikeRepository.existsByUserAndComment(user, comment)) {
            commentLikeRepository.save(CommentLike.builder().user(user).comment(comment).build());
            notificationService.pushToUser(comment.getUser().getUserId(), "새 좋아요", "내 댓글에 좋아요가 달렸어요");
        }
    }

    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        commentLikeRepository.findByUserAndComment(user, comment).ifPresent(commentLikeRepository::delete);
    }
}
