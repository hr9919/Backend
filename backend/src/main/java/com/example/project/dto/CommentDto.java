package com.example.project.dto;

import com.example.project.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private Long userId;
    private Long reviewId;
    private LocalDateTime createdAt;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .reviewId(comment.getReview().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}