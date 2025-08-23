package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.entity.Book;
import com.example.project.repository.BookRepository;
import com.example.project.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository; // ReviewRepository 추가

    public Book saveBook(BookDto dto) {
        return bookRepository.findByIsbn13(dto.getIsbn13())
                .orElseGet(() -> bookRepository.save(dto.toEntity()));
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다: " + id));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    
    // 인기 책 순위 산정 로직 (리뷰 수 기준)
    public List<BookDto> getPopularBooks() {
        return bookRepository.findAll().stream()
                .sorted(Comparator.comparingInt((Book b) -> b.getReviews().size()).reversed()) // ✅ Book 타입 명시
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    // 고도화된 책 검색 (새로 추가)
    public List<BookDto> searchBooksAdvanced(String keyword, String sort) {
        List<Book> books = bookRepository.findAll().stream()
                .filter(book -> book.getTitle().contains(keyword) || book.getAuthor().contains(keyword))
                .collect(Collectors.toList());

        if ("popular".equals(sort)) {
            return books.stream()
                    .sorted(Comparator.comparingInt((Book b) -> b.getReviews().size()).reversed()) // ✅ Book 타입 명시
                    .map(BookDto::fromEntity)
                    .collect(Collectors.toList());
        }

        return books.stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
    }
}
