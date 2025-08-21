package com.example.project.service;

import com.example.project.dto.ReviewDto;
import com.example.project.dto.request.CreateReviewRequest;
import com.example.project.dto.request.UpdateReviewRequest;
import com.example.project.entity.Book;
import com.example.project.entity.Review;
import com.example.project.entity.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.ReviewRepository;
import com.example.project.repository.UserRepository;
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

    @Transactional
    public ReviewDto create(CreateReviewRequest req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Book book = bookRepository.findById(req.getBookId())
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

        Review review = Review.builder()
                .content(req.getContent())
                .imageUrl(req.getImageUrl())
                .user(user)
                .book(book)
                .build();

        return ReviewDto.from(reviewRepository.save(review));
    }

    @Transactional
    public ReviewDto update(Long reviewId, Long me, UpdateReviewRequest req) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(me)) throw new RuntimeException("FORBIDDEN");

        review.setContent(req.getContent());
        review.setImageUrl(req.getImageUrl());

        return ReviewDto.from(review);
    }

    @Transactional(readOnly = true)
    public ReviewDto get(Long reviewId, Long me) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        return ReviewDto.from(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return reviewRepository.findByUser(user)
                .stream()
                .map(ReviewDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

        return reviewRepository.findByBook(book)
                .stream()
                .map(ReviewDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(userId)) throw new RuntimeException("FORBIDDEN");

        reviewRepository.delete(review);
    }
}