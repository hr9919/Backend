package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import com.example.project.events.LikeCreatedEvent; // ✅ 추가

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
     
    private final NotificationService notificationService;
    // 이벤트 발행용
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public void likeReview(Long reviewId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        if (likeRepository.existsByUserAndReview(user, review)) return;

        Like like = new Like(null, user, review, null, null);
        likeRepository.save(like);

        // ✅ 이벤트 발행
        eventPublisher.publishEvent(new LikeCreatedEvent(
                like.getLikeId(),
                userId,
                LikeCreatedEvent.TargetType.REVIEW,
                reviewId,
                null,
                review.getUser().getId()
        ));

        notificationService.pushToUser(review.getUser().getId(), "새 좋아요", "내 감상문에 좋아요가 달렸어요");
    
    }

    @Transactional
    public void unlikeReview(Long reviewId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Review review = reviewRepository.findById(reviewId).orElseThrow();

        likeRepository.deleteByUserAndReview(user, review);
    }

    @Transactional
    public void likeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (likeRepository.existsByUserAndComment(user, comment)) return;

        Like like = new Like(null, user, null, comment, null);
        likeRepository.save(like);

        /*notificationService.pushToUser(comment.getUser().getId(), "새 좋아요", "내 댓글에 좋아요가 달렸어요");*/
    }

    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        likeRepository.deleteByUserAndComment(user, comment);
    }
}
