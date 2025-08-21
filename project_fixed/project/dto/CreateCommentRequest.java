package com.example.project.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateCommentRequest {
    private Long reviewId;
    private Long userId;
    private String content;
    private Long parentCommentId;
}
