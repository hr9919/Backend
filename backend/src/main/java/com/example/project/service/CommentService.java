package com.example.project.service;

import com.example.project.dto.CommentDto;
import com.example.project.entity.Comment;
import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.ReviewRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    // 댓글 생성
    @Transactional
    public CommentDto create(Long userId, Long reviewId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .review(review)
                .build();

        return CommentDto.from(commentRepository.save(comment));
    }

    // 댓글 수정
    @Transactional
    public CommentDto update(Long commentId, Long userId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        comment.setContent(content);
        return CommentDto.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // 특정 리뷰의 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentDto> getByReview(Long reviewId) {
        return commentRepository.findByReviewId(reviewId)
                .stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());
    }
}
