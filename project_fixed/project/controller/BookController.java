package com.example.project.controller;

import com.example.project.dto.BookDto;
import com.example.project.entity.Book;
import com.example.project.service.AladinService;
import com.example.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;
    private final BookService bookService;

    @GetMapping("/search")
    public List<BookDto> searchBooks(@RequestParam String query) {
        return aladinService.searchBooks(query);
    }

    @PostMapping("/import")
    public BookDto importBook(@RequestParam String query, @RequestParam String isbn13) {
        List<BookDto> results = aladinService.searchBooks(query);

        return results.stream()
                .filter(book -> book.getIsbn13().equals(isbn13))
                .findFirst()
                .map(bookService::saveFromDto)
                .map(BookDto::new)
                .orElseThrow(() -> new RuntimeException("ÇØ´ç ISBN13ÀÇ Ã¥À» Ã£À» ¼ö ¾ø½À´Ï´Ù."));
    }
}
