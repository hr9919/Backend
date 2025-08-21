package com.example.project.dto;

import com.example.project.entity.Review;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewId;
    private String content;
    private String imageUrl;
    private int rating;
    private Long userId;
    private Long bookId;

    // Review 엔티티 → DTO 변환 메서드
    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())              // 수정: getReviewId() → getId()
                .content(review.getContent())
                .imageUrl(review.getImageUrl())
                .userId(review.getUser().getId())      // User 엔티티의 PK
                .bookId(review.getBook().getId())      // 수정: getBookId() → getId()
                .build();
    }
}
