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

    // DTO → Entity
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

    // Entity → DTO
    public static BookDto fromEntity(Book book) {
        return BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .pubDate(book.getPubDate())
                .isbn(book.getIsbn())
                .isbn13(book.getIsbn13())
                .cover(book.getCover())
                .link(book.getLink())
                .categoryName(book.getCategoryName())
                .itemPage(book.getItemPage())
                .build();
    }
}