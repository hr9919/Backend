package com.example.project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewRequest {
    private String content;   // 수정할 리뷰 본문
    private String imageUrl;  // 수정할 이미지 (선택)
    private int rating;   // 수정할 평점 (선택)
}
