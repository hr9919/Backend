package com.example.project.dto;

import com.example.project.entity.User;
import lombok.*;

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

    private int followerCount;
    private int followingCount;
    private int reviewCount;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .bio(user.getBio())
                .tagId(user.getTagId())
                .followerCount(user.getFollowers().size())
                .followingCount(user.getFollowing().size())
                .reviewCount(user.getReviews().size())
                .build();
    }
}