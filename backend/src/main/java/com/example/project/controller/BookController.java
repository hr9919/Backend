package com.example.project.controller;

import com.example.project.dto.BookDto;
import com.example.project.entity.Book;
import com.example.project.service.AladinService;
import com.example.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AladinService aladinService;

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody BookDto dto) {
        Book savedBook = bookService.saveBook(dto);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.ok(aladinService.searchBooks(keyword));
    }
}
