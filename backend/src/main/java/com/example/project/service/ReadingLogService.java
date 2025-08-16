package com.example.project.service;

import com.example.project.dto.ReadingLogDto;
import com.example.project.dto.ReadingLogStatsDto;
import com.example.project.entity.Book;
import com.example.project.entity.ReadingLog;
import com.example.project.entity.User;
import com.example.project.entity.UserBookProgress;
import com.example.project.exception.BookNotFoundException;
import com.example.project.exception.ReadingLogNotFoundException;
import com.example.project.repository.BookRepository;
import com.example.project.repository.ReadingLogRepository;
import com.example.project.repository.UserBookProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadingLogService {

    private final ReadingLogRepository logRepository;
    private final BookRepository bookRepository;
    private final UserBookProgressRepository progressRepository;
    private final ReadingGoalService goalService;

    // 독서 기록 추가 또는 수정
    @Transactional
    public ReadingLogDto addOrUpdateReadingLog(ReadingLogDto dto, User user) {
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(dto.getBookId()));

        ReadingLog log = dto.convertToEntity(user, book);
        ReadingLog saved = logRepository.save(log);

        // 독서 목표와 연동
        if (dto.getMinutesRead() != null && dto.getMinutesRead() > 0) {
            goalService.addReadingTimeByUser(user.getId(), dto.getMinutesRead());
        }

        // UserBookProgress 업데이트 (완독 상태 관리)
        UserBookProgress progress = progressRepository.findByUserAndBook(user, book)
                .orElseGet(() -> new UserBookProgress(user, book));

        // 누적 페이지 업데이트
        progress.setPagesRead(progress.getPagesRead() + (dto.getPagesRead() != null ? dto.getPagesRead() : 0));

        // 완독 체크
        if (!Boolean.TRUE.equals(progress.getCompleted()) && progress.getPagesRead() >= book.getTotalPages()) {
            progress.setCompleted(true);
            progress.setCompletedAt(LocalDateTime.now());

            // 목표 서비스 연동 (완독 처리)
            goalService.completeBookByUser(user.getId());
        }

        progressRepository.save(progress);

        return ReadingLogDto.fromEntity(saved);
    }

    // 사용자별 독서 기록 조회
    public List<ReadingLogDto> getLogsByUser(Long userId) {
        List<ReadingLog> logs = logRepository.findByUserId(userId);

        if (logs.isEmpty()) {
            throw new ReadingLogNotFoundException(userId);
        }

        return logs.stream()
                .map(ReadingLogDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 사용자별 독서 통계 조회
    public ReadingLogStatsDto getReadingStats(Long userId) {
        int totalPages = logRepository.sumPagesByUser(userId);
        int totalMinutes = logRepository.sumMinutesByUser(userId);
        int completedBooks = progressRepository.countByUserIdAndCompletedTrue(userId);

        LocalDate firstLogDate = logRepository.findFirstLogDateByUser(userId);
        LocalDate lastLogDate = logRepository.findLastLogDateByUser(userId);

        return new ReadingLogStatsDto(
                userId,
                totalPages,
                totalMinutes,
                completedBooks,
                firstLogDate,
                lastLogDate
        );
    }
}
