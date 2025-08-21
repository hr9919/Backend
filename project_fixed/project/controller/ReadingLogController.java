package com.example.project.controller;

import com.example.project.dto.ReadingLogDto;
import com.example.project.entity.Book;
import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import com.example.project.service.BookService;
import com.example.project.service.ReadingLogService;
import com.example.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ReadingLogController {

    private final UserService userService;
    private final BookService bookService;
    private final ReadingLogService logService;

    @PostMapping("/{userId}/books/{bookId}/logs")
    public ResponseEntity<ReadingLogDto> createOrUpdateLog(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestBody ReadingLogDto dto) {

        User user = userService.findById(userId);
        Book book = bookService.findById(bookId);
        ReadingLog log = dto.toEntity(user, book);
        ReadingLog savedLog = logService.addOrUpdateReadingLog(user, book, log);

        return ResponseEntity.ok(new ReadingLogDto(savedLog));
    }

    @GetMapping("/{userId}/logs")
    public ResponseEntity<List<ReadingLogDto>> getLogsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        List<ReadingLogDto> logs = logService.getLogsByUser(user)
                .stream()
                .map(ReadingLogDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{userId}/logs/stats")
    public ResponseEntity<?> getReadingStats(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(logService.getReadingStats(user));
    }
}
