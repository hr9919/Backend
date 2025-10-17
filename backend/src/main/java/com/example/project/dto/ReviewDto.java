package com.example.project.dto;

import com.example.project.entity.Review;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long reviewId;
    private String content;
    private List<String> reviewImageUrls;
    private Integer rating;

    private Long userId;
    private Long bookId;

    // ✅ 사용자 표시용 필드
    private String nickname;         // String
    private String userProfileImage; // URL
    private String tagId;            // 원본 핸들(프론트에서 '@' 붙임)

    private String createdAt;
    private String updatedAt;
    private List<String> hashtags;

    // 공유 링크
    private String shareUrl;

    /** 기존 사용처 호환용 (shareUrl 미설정) */
    public static ReviewDto from(Review review) {
        return from(review, null);
    }

    /** 공유 링크까지 포함해서 만들고 싶을 때 사용 */
    public static ReviewDto from(Review review, String shareUrl) {
        // ✅ user 지역변수 선언 (컴파일 에러 원인 해결)
        var user = review.getUser();

        return ReviewDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .reviewImageUrls(review.getReviewImageUrls())
                .rating(review.getRating())
                .userId(user != null ? user.getId() : null)
                .bookId(review.getBook() != null ? review.getBook().getId() : null)

                // ✅ 닉네임/프로필/태그
                .nickname(user != null ? user.getNickname() : null)
                .userProfileImage(user != null ? user.getProfileImageUrl() : null) // <-- getProfileImageUrl 로 수정
                .tagId(user != null ? sanitizeTagId(user.getTagId()) : null)

                .createdAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null)
                .updatedAt(review.getUpdatedAt() != null ? review.getUpdatedAt().toString() : null)
                .hashtags(review.getHashtags() != null
                        ? review.getHashtags().stream()
                            .map(rh -> "#" + rh.getHashtag().getTagText())
                            .collect(Collectors.toList())
                        : List.of())
                .shareUrl(shareUrl)
                .build();
    }

    // ✅ DB에 '@haerim' 형태가 섞여 있을 대비 (프론트에서 @ 붙이므로 제거)
    private static String sanitizeTagId(String raw) {
        if (raw == null) return null;
        String t = raw.trim();
        return t.startsWith("@") ? t.substring(1) : t;
    }
}
