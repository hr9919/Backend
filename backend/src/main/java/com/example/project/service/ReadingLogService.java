package com.example.project.service;

import com.example.project.dto.ReadingLogStatsDto;
import com.example.project.entity.Book;
import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import com.example.project.repository.ReadingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingLogService {

    private final ReadingLogRepository logRepository;

    // 기록 추가/업데이트
    public ReadingLog addOrUpdateReadingLog(User user, Book book, ReadingLog log) {
        log.setUser(user);
        log.setBook(book);
        return logRepository.save(log);
    }

    // 사용자별 기록 조회
    public List<ReadingLog> getLogsByUser(User user) {
        return logRepository.findByUser(user);
    }

    // 독서 통계 조회
    public ReadingLogStatsDto getReadingStats(User user) {
        int totalPages = logRepository.sumPagesByUser(user);
        int totalMinutes = logRepository.sumMinutesByUser(user);
        int completedBooks = logRepository.countCompletedBooksByUser(user);
        LocalDate firstLogDate = logRepository.findFirstLogDateByUser(user);
        LocalDate lastLogDate = logRepository.findLastLogDateByUser(user);

        return new ReadingLogStatsDto(user.getId(), totalPages, totalMinutes, completedBooks, firstLogDate, lastLogDate);
    }
}
