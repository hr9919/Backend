package com.example.project.dto;

import com.example.project.entity.Group;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupDto {
    private Long id;
    private String name;
    private String description;
    private String groupImageUrl;
    private Long ownerId;
    private String category;
    private int memberCount;
    private LocalDateTime createdAt;

    public static GroupDto fromEntity(Group group, int memberCount) {
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .groupImageUrl(group.getGroupImageUrl())
                .ownerId(group.getOwner().getId())
                .category(group.getCategory())
                .memberCount(memberCount)
                .createdAt(group.getCreatedAt())
                .build();
    }
}
