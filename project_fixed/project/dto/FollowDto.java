package com.example.project.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FollowDto {
    private Long followId;
    private Long followerId;
    private Long followingId;
}
