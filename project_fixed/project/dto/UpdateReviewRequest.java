package com.example.project.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdateReviewRequest {
    private String content;
    private String imageUrl;
    private List<String> hashtags;
}
