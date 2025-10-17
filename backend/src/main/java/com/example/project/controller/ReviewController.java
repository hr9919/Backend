package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.ReviewDto;
import com.example.project.dto.PopularHashtagDto; // ✅ 추가
import com.example.project.dto.request.CreateReviewRequest;
import com.example.project.dto.request.UpdateReviewRequest;
import com.example.project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;                 // ✅ 페이징
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성 (JSON) - 해시태그/이미지 URL도 함께 받을 수 있게 확장
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> create(
            @RequestBody CreateReviewRequest req,
            @RequestAttribute Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.create(
                        userId,
                        req.getBookId(),                      // ✅ null 허용
                        req.getContent(),                     // 필수
                        req.getRating(),                      // ✅ Integer, null 허용
                        null,                                  // files: JSON 경로에서는 업로드 X
                        req.getHashtags(),                    // ✅ 해시태그 지원
                        req.getImageUrl() != null ? List.of(req.getImageUrl()) : req.getImageUrls() // ✅ 이미지 URL 병합
                )
        ));
    }

    // (선택) 리뷰 생성 + 이미지 + 해시태그를 한 번에 멀티파트로
    @PostMapping(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ReviewDto>> createMultipart(
            @RequestPart(value = "bookId", required = false) Long bookId,     // ✅ null 허용
            @RequestPart("content") String content,
            @RequestPart(value = "rating", required = false) Integer rating,   // ✅ null 허용
            @RequestPart(value = "hashtags", required = false) List<String> hashtags,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "imageUrls", required = false) List<String> imageUrls, // ✅ 이미 업로드된 URL
            @RequestAttribute Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.create(userId, bookId, content, rating, files, hashtags, imageUrls)
        ));
    }

    // 리뷰 수정 (부분 업데이트 지원)
    @PutMapping("/{reviewId}")
public ResponseEntity<ApiResponse<ReviewDto>> update(
        @PathVariable Long reviewId,
        @RequestAttribute Long userId,
        @RequestBody UpdateReviewRequest req) {

    return ResponseEntity.ok(ApiResponse.success(
            reviewService.update(
                    reviewId,
                    userId,
                    req.getContent(),        // null이면 미변경
                    req.getRating(),         // null이면 미변경
                    req.getHashtags(),       // null이면 미변경
                    req.getImageUrls(),      // null이면 미변경
                    null,                    // files: JSON 업데이트에선 없음
                    Boolean.FALSE,           // clearImages 기본 false
                    req.getBookId()          
            )
    ));
}


    // (선택) 멀티파트 수정: 이미지 업로드/교체까지
    @PutMapping(value = "/{reviewId}/multipart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ApiResponse<ReviewDto>> updateMultipart(
        @PathVariable Long reviewId,
        @RequestAttribute Long userId,
        @RequestPart(value = "content", required = false) String content,
        @RequestPart(value = "rating", required = false) Integer rating,
        @RequestPart(value = "hashtags", required = false) List<String> hashtags,
        @RequestPart(value = "imageUrls", required = false) List<String> imageUrls,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @RequestPart(value = "clearImages", required = false) Boolean clearImages,
        @RequestPart(value = "bookId", required = false) Long bookId   // ✅ 추가
) {
    return ResponseEntity.ok(ApiResponse.success(
            reviewService.update(
                    reviewId, userId, content, rating, hashtags, imageUrls, files,
                    clearImages != null ? clearImages : false,   // 원시형 boolean로 넘겨도 OK
                    bookId                                      // ✅ 마지막 인자로 전달
            )
    ));
}

    // 리뷰 단건 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> get(@PathVariable Long reviewId) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.get(reviewId)
        ));
    }

    // 전체 리뷰 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getAll()
        ));
    }

    // 특정 유저의 리뷰 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getByUser(userId)
        ));
    }

    // 특정 책의 리뷰 조회
    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getByBook(bookId)
        ));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long reviewId,
            @RequestAttribute Long userId) {
        reviewService.delete(reviewId, userId);
        return ResponseEntity.noContent().build();
    }

    // 인기 해시태그 Top 20
    @GetMapping("/popular-tags")
    public ResponseEntity<ApiResponse<List<PopularHashtagDto>>> popularTags() {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.getPopularHashtagsTop20()
        ));
    }

    // 해시태그 포함 리뷰 검색 (최신순, 페이징)
    @GetMapping("/search-by-hashtag")
    public ResponseEntity<ApiResponse<Page<ReviewDto>>> searchByHashtag(
            @RequestParam String hashtag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                reviewService.searchReviewsByHashtag(hashtag, page, size)
        ));
    }
}
