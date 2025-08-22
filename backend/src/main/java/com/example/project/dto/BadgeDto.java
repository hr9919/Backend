package com.example.project.dto;

import com.example.project.entity.Badge;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadgeDto {
    private Long id;
    private String badgeName;
    private String tier;
    private LocalDateTime createdAt;
    
    public static BadgeDto from(Badge badge) {
        return BadgeDto.builder()
                .id(badge.getId())
                .badgeName(badge.getBadgeName())
                .tier(badge.getTier())
                .createdAt(badge.getCreatedAt())
                .build();
    }
}
