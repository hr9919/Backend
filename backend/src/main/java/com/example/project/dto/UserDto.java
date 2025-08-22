package com.example.project.dto;

import com.example.project.entity.User;
import com.example.project.entity.Badge; // Badge 엔티티 import
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    private String username;
    private String profileImage;
    private String bio;
    private String tagId;
    
    // 새로 추가된 필드
    private int level;
    private int experience;
    private List<BadgeDto> badges; // BadgeDto 목록

    private int followerCount;
    private int followingCount;
    private int reviewCount;

    public static UserDto from(User user) {
        // Badge 엔티티를 BadgeDto로 변환
        List<BadgeDto> badgeDtos = user.getBadges().stream()
                .map(BadgeDto::from)
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .tagId(user.getTagId())
                .level(user.getLevel()) // 레벨 추가
                .experience(user.getExperience()) // 경험치 추가
                .badges(badgeDtos) // 배지 목록 추가
                .followerCount(user.getFollowers().size())
                .followingCount(user.getFollowing().size())
                .reviewCount(user.getReviews().size())
                .build();
    }
}
