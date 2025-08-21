package com.example.project.service;

import com.example.project.dto.ReadingLogDto;
import com.example.project.dto.ReadingLogStatsDto;
import com.example.project.entity.Book;
import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.ReadingLogRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadingLogService {

    private final ReadingLogRepository logRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ReadingLogDto create(Long userId, Long bookId, ReadingLogDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("책을 찾을 수 없습니다."));

        ReadingLog log = ReadingLog.builder()
                .user(user)
                .book(book)
                .pagesRead(dto.getPagesRead())
                .minutesRead(dto.getMinutesRead())
                .readAt(dto.getReadAt())
                .build();

        return ReadingLogDto.fromEntity(logRepository.save(log));
    }

    @Transactional(readOnly = true)
    public List<ReadingLogDto> getByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return logRepository.findByUser(user)
                .stream()
                .map(ReadingLogDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReadingLogDto update(Long logId, ReadingLogDto dto) {
        ReadingLog log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("독서 기록을 찾을 수 없습니다."));

        log.setPagesRead(dto.getPagesRead());
        log.setMinutesRead(dto.getMinutesRead());
        log.setReadAt(dto.getReadAt());

        return ReadingLogDto.fromEntity(logRepository.save(log));
    }

    @Transactional
    public void delete(Long userId, Long logId) {
        ReadingLog log = logRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("독서 기록을 찾을 수 없습니다. ID=" + logId));

        if (!log.getUser().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        logRepository.delete(log);
    }

    @Transactional(readOnly = true)
    public ReadingLogStatsDto getStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        int totalPages = logRepository.sumPagesByUser(user);
        int totalMinutes = logRepository.sumMinutesByUser(user);
        int completedBooks = logRepository.countCompletedBooksByUser(user);
        LocalDate firstLogDate = logRepository.findFirstLogDateByUser(user);
        LocalDate lastLogDate = logRepository.findLastLogDateByUser(user);

        return new ReadingLogStatsDto(userId, totalPages, totalMinutes, completedBooks, firstLogDate, lastLogDate);
    }
}