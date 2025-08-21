package com.example.project.dto;

import com.example.project.entity.Book;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String title;
    private String author;
    private String publisher;
    private String pubDate;
    private String isbn;
    private String isbn13;
    private String cover;
    private String link;
    private String categoryName;
    private int itemPage;

    public Book toEntity() {
        return Book.builder()
                .title(this.title)
                .author(this.author)
                .publisher(this.publisher)
                .pubDate(this.pubDate)
                .isbn(this.isbn)
                .isbn13(this.isbn13)
                .cover(this.cover)
                .link(this.link)
                .categoryName(this.categoryName)
                .itemPage(this.itemPage)
                .build();
    }
}
