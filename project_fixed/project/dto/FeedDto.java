package com.example.project.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FeedDto {
    private Long feedId;
    private Long reviewId;
    private Long userId;
    private LocalDateTime createdAt;
    private ReviewDto review;
}
