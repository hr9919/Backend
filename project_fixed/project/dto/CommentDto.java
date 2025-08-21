package com.example.project.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentDto {
    private Long commentId;
    private Long reviewId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private long likeCount;
    private boolean likedByMe;
    private Long parentCommentId;
}
