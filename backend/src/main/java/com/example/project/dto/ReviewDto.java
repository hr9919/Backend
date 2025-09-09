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
    private int rating;
    private Long userId;
    private Long bookId;
    private String createdAt;
    private String updatedAt;
    private List<String> hashtags;

    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .reviewImageUrls(review.getReviewImageUrls())
                .rating(review.getRating())
                .userId(review.getUser().getId())
                .bookId(review.getBook().getId())
                .createdAt(review.getCreatedAt().toString())
                .updatedAt(review.getUpdatedAt().toString())
                .hashtags(review.getHashtags() != null
                        ? review.getHashtags().stream()
                                .map(rh -> rh.getHashtag().getTagText())
                                .collect(Collectors.toList())
                        : List.of())
                .build();
    }
}
