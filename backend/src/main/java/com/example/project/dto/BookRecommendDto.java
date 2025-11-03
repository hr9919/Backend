package com.example.project.dto;

import com.example.project.entity.BookRecommend;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookRecommendDto {
    private Long id;
    private String bookTitle;
    private String author;
    private String bookCoverUrl;
    private Float hybridScore;
    private LocalDateTime createdAt;

    public static BookRecommendDto fromEntity(BookRecommend e) {
        return BookRecommendDto.builder()
                .id(e.getId())
                .bookTitle(e.getBookTitle())
                .author(e.getAuthor())
                .bookCoverUrl(e.getBookCoverUrl())
                .hybridScore(e.getHybridScore())
                .createdAt(e.getCreatedAt())
                .build();
    }
}