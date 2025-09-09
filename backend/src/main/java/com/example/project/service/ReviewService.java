package com.example.project.service;

import com.example.project.dto.ReviewDto;
import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final HashtagRepository hashtagRepository;
    private final StorageService storageService;

    @Transactional
    public ReviewDto create(Long userId, Long bookId, String content, int rating, List<MultipartFile> files, List<String> hashtagNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

        Review review = Review.builder()
                .user(user)
                .book(book)
                .content(content)
                .rating(rating)
                .reviewImageUrls(new ArrayList<>())
                .hashtags(new ArrayList<>())
                .build();

        if (files != null) {
            for (MultipartFile file : files) {
                String fileName = "review/" + UUID.randomUUID();
                String uploadedUrl = storageService.upload(file, fileName);
                review.getReviewImageUrls().add(uploadedUrl);
            }
        }

        if (hashtagNames != null) {
            for (String name : hashtagNames) {
                Hashtag hashtag = hashtagRepository.findByTagText(name)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagText(name).build()));
                ReviewHashtag rh = new ReviewHashtag();
                rh.setHashtag(hashtag);
                rh.setReview(review);
                review.getHashtags().add(rh);
            }
        }

        return ReviewDto.from(reviewRepository.save(review));
    }

    @Transactional
    public ReviewDto uploadReviewImages(Long reviewId, List<MultipartFile> files) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        for (MultipartFile file : files) {
            String fileName = "review/" + reviewId + "_" + UUID.randomUUID();
            String uploadedUrl = storageService.upload(file, fileName);
            review.getReviewImageUrls().add(uploadedUrl);
        }

        return ReviewDto.from(review);
    }

    @Transactional
    public ReviewDto update(Long reviewId, Long userId, String content, int rating, List<String> hashtagNames) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        if (!review.getUser().getId().equals(userId)) throw new RuntimeException("FORBIDDEN");

        review.setContent(content);
        review.setRating(rating);

        review.getHashtags().clear();
        if (hashtagNames != null) {
            for (String name : hashtagNames) {
                Hashtag hashtag = hashtagRepository.findByTagText(name)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagText(name).build()));
                ReviewHashtag rh = new ReviewHashtag();
                rh.setHashtag(hashtag);
                rh.setReview(review);
                review.getHashtags().add(rh);
            }
        }

        return ReviewDto.from(review);
    }

    @Transactional(readOnly = true)
    public ReviewDto get(Long reviewId) {
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
