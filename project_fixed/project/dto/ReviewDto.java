package com.example.project.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDto {
    private Long reviewId;
    private String content;
    private String imageUrl;
    private Long userId;
    private Long bookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> hashtags;
    private long likeCount;
    private boolean likedByMe;
}
