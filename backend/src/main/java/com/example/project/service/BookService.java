package com.example.project.service;

import com.example.project.dto.BookDto;
import com.example.project.entity.Book;
import com.example.project.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

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
}
