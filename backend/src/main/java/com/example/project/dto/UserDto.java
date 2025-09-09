package com.example.project.dto;

import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
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
    private String profileImageUrl;
    private String bio;
    private String tagId;

    private int level;
    private int experience;
    private List<BadgeDto> badges;

    private int followerCount;
    private int followingCount;
    private int reviewCount;

    private List<BookDto> recentBooks;      // 최근 읽은 책 3권
    private List<ReviewDto> recentReviews;  // 최근 작성 리뷰 3개

    public static UserDto from(User user) {
        List<BadgeDto> badgeDtos = user.getBadges().stream()
                .map(BadgeDto::from)
                .collect(Collectors.toList());

        // 최근 작성 리뷰 3개 (작성일 내림차순)
        List<ReviewDto> recentReviews = user.getReviews().stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(3)
                .map(ReviewDto::from)
                .collect(Collectors.toList());

        // 최근 읽은 책 3권 (ReadingLog 기준 내림차순)
        List<BookDto> recentBooks = user.getReadingLogs().stream()
                .sorted((l1, l2) -> l2.getReadAt().compareTo(l1.getReadAt()))
                .map(ReadingLog::getBook)
                .distinct() // 같은 책 중복 제거
                .limit(3)
                .map(BookDto::from)
                .collect(Collectors.toList());

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .tagId(user.getTagId())
                .level(user.getLevel())
                .experience(user.getExperience())
                .badges(badgeDtos)
                .followerCount(user.getFollowers().size())
                .followingCount(user.getFollowing().size())
                .reviewCount(user.getReviews().size())
                .recentBooks(recentBooks)
                .recentReviews(recentReviews)
                .build();
    }
}
