package com.example.project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {
    private Long bookId;      // 어떤 책에 대한 리뷰인지
    private String content;   // 리뷰 본문
    private String imageUrl;  // 첨부 이미지 (선택)
    private int rating;   // 평점 (선택, 1~5 정도)
}