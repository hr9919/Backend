package com.example.project.dto;

import com.example.project.entity.GroupPost;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupPostDto {
    private Long id;
    private Long groupId;
    private Long userId;
    private String postType;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

    public static GroupPostDto fromEntity(GroupPost post) {
        return GroupPostDto.builder()
                .id(post.getId())
                .groupId(post.getGroup().getId())
                .userId(post.getUser().getId())
                .postType(post.getPostType().name())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
