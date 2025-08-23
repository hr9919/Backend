package com.example.project.controller;

import com.example.project.dto.BookDto;
import com.example.project.entity.Book;
import com.example.project.service.AladinService;
import com.example.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AladinService aladinService;

    // 책 저장
    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody BookDto dto) {
        Book savedBook = bookService.saveBook(dto);
        return ResponseEntity.ok(savedBook);
    }

    // 모든 책 조회
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.findAll().stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(books);
    }

    // 특정 책 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(BookDto.fromEntity(book));
    }

    // 알라딘 API 책 검색 (기존)
    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(aladinService.searchBooks(keyword));
    }

    // 인기 책 순위 조회 (새로 추가)
    @GetMapping("/popular")
    public ResponseEntity<List<BookDto>> getPopularBooks() {
        List<BookDto> popularBooks = bookService.getPopularBooks();
        return ResponseEntity.ok(popularBooks);
    }

    // 고도화된 책 검색 (새로 추가)
    @GetMapping("/search-advanced")
    public ResponseEntity<List<BookDto>> searchBooksAdvanced(
        @RequestParam String keyword,
        @RequestParam(required = false) String sort) {
        List<BookDto> books = bookService.searchBooksAdvanced(keyword, sort);
        return ResponseEntity.ok(books);
    }
}
