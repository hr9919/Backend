package com.example.project.dto;

import com.example.project.entity.Book;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;
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

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.pubDate = book.getPubDate();
        this.isbn = book.getIsbn();
        this.isbn13 = book.getIsbn13();
        this.cover = book.getCover();
        this.link = book.getLink();
        this.categoryName = book.getCategoryName();
        this.itemPage = book.getItemPage();
    }
}
