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

    // 인기순 정렬을 위한 필드 추가
    private Integer popularityScore;

    // Book 엔티티로 변환
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

    // Book 엔티티를 DTO로 변환
    public static BookDto from(Book book) {
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
                .popularityScore(book.getReviews() != null ? book.getReviews().size() : 0) // 리뷰 수를 인기 점수로 사용
                .build();
    }
}
