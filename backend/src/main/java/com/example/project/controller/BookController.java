package com.example.project.controller;

import com.example.project.dto.BookDto;
import com.example.project.service.AladinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final AladinService aladinService;

    @GetMapping("/search")
    public List<BookDto> searchBooks(@RequestParam String query) {
        return aladinService.searchBooks(query);
    }
}
