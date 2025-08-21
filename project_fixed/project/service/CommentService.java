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
public class CommentService {
    private final NotificationService notificationService;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentDto create(CreateCommentRequest req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow();
        Review review = reviewRepository.findById(req.getReviewId()).orElseThrow();
        Comment parent = null;
        if (req.getParentCommentId() != null) {
            parent = commentRepository.findById(req.getParentCommentId()).orElseThrow();
        }
        Comment c = Comment.builder().user(user).review(review).content(req.getContent()).parentComment(parent).build();
        c = commentRepository.save(c);
        notificationService.pushToUser(review.getUser().getUserId(), "새 댓글", "내 감상문에 댓글이 달렸어요");
        return toDto(c, user);
    }

    @Transactional
    public CommentDto update(Long commentId, Long me, String content) {
        Comment c = commentRepository.findById(commentId).orElseThrow();
        if (!c.getUser().getUserId().equals(me)) throw new RuntimeException("FORBIDDEN");
        c.setContent(content);
        return toDto(c, c.getUser());
    }

    @Transactional
    public void delete(Long commentId, Long me) {
        Comment c = commentRepository.findById(commentId).orElseThrow();
        if (!c.getUser().getUserId().equals(me)) throw new RuntimeException("FORBIDDEN");
        commentRepository.delete(c);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> listByReview(Long reviewId, Long me) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        User meUser = me != null ? userRepository.findById(me).orElse(null) : null;
        return commentRepository.findByReviewOrderByCreatedAtAsc(review).stream()
                .map(c -> toDto(c, meUser)).collect(Collectors.toList());
    }

    private CommentDto toDto(Comment c, User meUser) {
        long likeCount = commentLikeRepository.countByComment(c);
        boolean likedByMe = false;
        if (meUser != null) {
            likedByMe = commentLikeRepository.existsByUserAndComment(meUser, c);
        }
        return CommentDto.builder()
                .commentId(c.getCommentId())
                .reviewId(c.getReview().getReviewId())
                .userId(c.getUser().getUserId())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .likeCount(likeCount)
                .likedByMe(likedByMe)
                .parentCommentId(c.getParentComment() != null ? c.getParentComment().getCommentId() : null)
                .build();
    }
}
