package com.example.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikerDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
}
