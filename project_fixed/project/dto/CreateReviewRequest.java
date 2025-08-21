package com.example.project.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateReviewRequest {
    private String content;
    private String imageUrl;
    private Long userId;
    private Long bookId;
    private List<String> hashtags; // '#없이' 텍스트만
}
