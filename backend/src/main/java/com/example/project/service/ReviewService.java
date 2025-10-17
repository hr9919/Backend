package com.example.project.service;

import com.example.project.common.ShareLinkService;          // ✅
import com.example.project.dto.ReviewDto;
import com.example.project.dto.PopularHashtagDto;
import com.example.project.entity.*;
import com.example.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final ReviewHashtagRepository reviewHashtagRepository;

    private final ShareLinkService shareLinkService;

    // ✅ 추가: 리뷰 생성 후 목표 업데이트를 위해 주입
    private final ReadingGoalService readingGoalService;

    // ---------------------------
    // 내부 유틸
    // ---------------------------
    private String normalizeTag(String raw) {
        if (raw == null) return null;
        String t = raw.trim();
        if (t.startsWith("#")) t = t.substring(1);
        return t;
    }

    private String normalizeTagForQuery(String raw) {
        if (raw == null) return null;
        String t = raw.trim();
        if (t.startsWith("#")) t = t.substring(1);
        return t;
    }

    private void linkHashtags(Review review, List<String> hashtagNames) {
        if (hashtagNames == null) return;
        Set<Long> existing = review.getHashtags().stream()
                .map(rh -> rh.getHashtag().getHashtagId())
                .collect(Collectors.toSet());

        hashtagNames.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeTag)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .forEach(name -> {
                    Hashtag tag = hashtagRepository.findByTagText(name)
                            .orElseGet(() -> hashtagRepository.save(Hashtag.builder().tagText(name).build()));
                    if (!existing.contains(tag.getHashtagId())) {
                        ReviewHashtag rh = ReviewHashtag.builder()
                                .review(review)
                                .hashtag(tag)
                                .build();
                        review.getHashtags().add(rh); // cascade로 함께 저장
                    }
                });
    }

    private List<String> mergeImageUrls(List<String> base, List<String> extra) {
        List<String> out = new ArrayList<>();
        if (base != null) out.addAll(base);
        if (extra != null) out.addAll(extra);
        return out.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    // ---------------------------
    // 생성/수정
    // ---------------------------
    @Transactional
    public ReviewDto create(Long userId, Long bookId, String content, Integer rating,
                            List<MultipartFile> files, List<String> hashtagNames, List<String> imageUrls) {

        if (content == null || content.isBlank()) {
            throw new RuntimeException("리뷰 내용을 입력해주세요.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Book book = null;
        if (bookId != null) {
            book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
        }

        // 파일 업로드 → URL 병합
        List<String> urls = new ArrayList<>(imageUrls != null ? imageUrls : List.of());
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = "review/" + UUID.randomUUID();
                String uploadedUrl = storageService.upload(file, fileName);
                urls.add(uploadedUrl);
            }
        }
        urls = mergeImageUrls(urls, null);

        Review review = Review.builder()
                .user(user)
                .book(book)                 // null 허용
                .content(content)
                .rating(rating)             // Integer, null 허용
                .reviewImageUrls(urls)
                .hashtags(new ArrayList<>())
                .build();

        // 해시태그 연결
        linkHashtags(review, hashtagNames);

        Review saved = reviewRepository.save(review);

        // ✅ 리뷰 생성 직후 목표 업데이트 트리거
        readingGoalService.onReviewCreated(userId);

        String shareUrl = shareLinkService.reviewUrl(saved.getId());
        return ReviewDto.from(saved, shareUrl);
    }

    @Transactional
    public ReviewDto uploadReviewImages(Long reviewId, List<MultipartFile> files) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileName = "review/" + reviewId + "_" + UUID.randomUUID();
                String uploadedUrl = storageService.upload(file, fileName);
                review.getReviewImageUrls().add(uploadedUrl);
            }
            review.setReviewImageUrls(mergeImageUrls(review.getReviewImageUrls(), null));
        }

        Review saved = reviewRepository.save(review);
        String shareUrl = shareLinkService.reviewUrl(saved.getId());
        return ReviewDto.from(saved, shareUrl);
    }

    @Transactional
    public ReviewDto update(Long reviewId, Long userId, String content, Integer rating,
                            List<String> hashtagNames, List<String> imageUrls,
                            List<MultipartFile> files, boolean clearImages, Long bookId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        if (!Objects.equals(review.getUser().getId(), userId)) {
            throw new RuntimeException("FORBIDDEN");
        }

        if (content != null) review.setContent(content);
        if (rating != null) review.setRating(rating);

        if (bookId != null) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
            review.setBook(book);
        }

        // 이미지 처리
        if (clearImages) {
            review.setReviewImageUrls(new ArrayList<>());
        }
        if (imageUrls != null) {
            review.setReviewImageUrls(mergeImageUrls(review.getReviewImageUrls(), imageUrls));
        }
        if (files != null && !files.isEmpty()) {
            List<String> uploaded = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileName = "review/" + reviewId + "_" + UUID.randomUUID();
                String uploadedUrl = storageService.upload(file, fileName);
                uploaded.add(uploadedUrl);
            }
            review.setReviewImageUrls(mergeImageUrls(review.getReviewImageUrls(), uploaded));
        }

        // 해시태그 전체 교체
        if (hashtagNames != null) {
            reviewHashtagRepository.deleteByReview(review);
            review.getHashtags().clear();
            reviewRepository.flush();

            hashtagNames.stream()
                    .filter(Objects::nonNull)
                    .map(this::normalizeTag)
                    .filter(s -> s != null && !s.isBlank())
                    .distinct()
                    .forEach(name -> {
                        Hashtag tag = hashtagRepository.findByTagText(name)
                                .orElseGet(() -> hashtagRepository.save(
                                        Hashtag.builder().tagText(name).build()
                                ));
                        ReviewHashtag rh = ReviewHashtag.builder()
                                .review(review)
                                .hashtag(tag)
                                .build();
                        review.getHashtags().add(rh);
                    });
        }

        Review saved = reviewRepository.save(review);

        // ⚠️ update 시에는 "리뷰 개수"를 늘리지 않으므로 기본적으로 목표 업데이트는 트리거하지 않음.
        // (정책적으로 "최초 등록만 카운트" 기준.)

        String shareUrl = shareLinkService.reviewUrl(saved.getId());
        return ReviewDto.from(saved, shareUrl);
    }

    // ---------------------------
    // 조회
    // ---------------------------
    @Transactional(readOnly = true)
    public ReviewDto get(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        String shareUrl = shareLinkService.reviewUrl(reviewId);
        return ReviewDto.from(review, shareUrl);
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll().stream()
                .map(r -> ReviewDto.from(r, shareLinkService.reviewUrl(r.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return reviewRepository.findByUser(user).stream()
                .map(r -> ReviewDto.from(r, shareLinkService.reviewUrl(r.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));
        return reviewRepository.findByBook(book).stream()
                .map(r -> ReviewDto.from(r, shareLinkService.reviewUrl(r.getId())))
                .collect(Collectors.toList());
    }

    // ---------------------------
    // 인기 해시태그 Top 20
    // ---------------------------
    @Transactional(readOnly = true)
    public List<PopularHashtagDto> getPopularHashtagsTop20() {
        Page<ReviewHashtagRepository.PopularHashtagProjection> page =
                reviewHashtagRepository.findPopularTags(PageRequest.of(0, 20));
        return page.getContent().stream()
                .map(p -> PopularHashtagDto.builder()
                        .tag("#" + p.getTag())
                        .count(p.getCnt())
                        .build())
                .toList();
    }

    // ---------------------------
    // 해시태그로 리뷰 최신순 검색 (페이징)
    // ---------------------------
    @Transactional(readOnly = true)
    public Page<ReviewDto> searchReviewsByHashtag(String hashtag, int page, int size) {
        String norm = normalizeTagForQuery(hashtag);
        PageRequest pr = PageRequest.of(Math.max(0, page), Math.max(1, size));
        Page<Review> result = reviewHashtagRepository.findReviewsByTagOrderByCreatedAtDesc(norm, pr);
        return result.map(r -> ReviewDto.from(r, shareLinkService.reviewUrl(r.getId())));
    }

    // ---------------------------
    // 삭제
    // ---------------------------
    @Transactional
    public void delete(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
        Long ownerId = review.getUser().getId();
        if (!Objects.equals(ownerId, userId)) {
            throw new RuntimeException("FORBIDDEN");
        }
        reviewRepository.delete(review);
    }
}
