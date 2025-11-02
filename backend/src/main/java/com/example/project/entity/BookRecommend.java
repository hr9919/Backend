package com.example.project.entity;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_recommend")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BookRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String bookTitle;
    private String author;
    private String bookCoverUrl;
    private Float hybridScore;
    private LocalDateTime createdAt;
}