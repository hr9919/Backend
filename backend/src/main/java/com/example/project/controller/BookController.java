package com.example.project.controller;

import com.example.project.dto.BookDto;
import com.example.project.service.AladinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    private final AladinService aladinService;

    public BookController(AladinService aladinService) {
        this.aladinService = aladinService;
    }

    @GetMapping("/books/search")
    public List<BookDto> searchBooks(@RequestParam String query) throws Exception {
        return aladinService.searchBooks(query);
    }
}
