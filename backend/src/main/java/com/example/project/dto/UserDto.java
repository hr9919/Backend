package com.example.project.dto;

import com.example.project.entity.User;
import com.example.project.entity.Badge; 
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
    
    private int level;
    private int experience;
    private List<BadgeDto> badges;

    private int followerCount;
    private int followingCount;
    private int reviewCount;

    private List<BookDto> recentBooks;
    private List<ReviewDto> recentReviews;

    public static UserDto from(User user) {
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
                .level(user.getLevel())
                .experience(user.getExperience())
                .badges(badgeDtos)
                .followerCount(user.getFollowers().size())
                .followingCount(user.getFollowing().size())
                .reviewCount(user.getReviews().size())
                .build();
    }
}
