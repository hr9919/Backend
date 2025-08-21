package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.entity.Book;
import com.example.project.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다. ID=" + id));
    }

    // BookDto → Book 엔티티 저장
    public Book saveFromDto(BookDto dto) {
        // 이미 같은 ISBN13 이 있으면 중복 저장 방지
        return bookRepository.findByIsbn13(dto.getIsbn13())
                .orElseGet(() -> bookRepository.save(Book.builder()
                        .title(dto.getTitle())
                        .author(dto.getAuthor())
                        .publisher(dto.getPublisher())
                        .pubDate(dto.getPubDate())
                        .isbn(dto.getIsbn())
                        .isbn13(dto.getIsbn13())
                        .cover(dto.getCover())
                        .link(dto.getLink())
                        .categoryName(dto.getCategoryName())
                        .itemPage(dto.getItemPage())
                        .build()));
    }
}
