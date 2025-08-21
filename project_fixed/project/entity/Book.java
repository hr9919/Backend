package com.example.project.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private String publisher; // 추가
    private String pubDate;
    private String isbn;
    private String isbn13;
    private String cover;
    private String link;
    private String categoryName;
    private int itemPage;
}

