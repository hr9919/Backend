package com.example.project.service;

import com.example.project.dto.ReportDto;
import com.example.project.entity.User;
import com.example.project.repository.ReadingLogRepository;
import com.example.project.repository.ReadingGoalRepository;
import com.example.project.repository.ReviewRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReadingLogRepository readingLogRepository;
    private final ReadingGoalRepository readingGoalRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ReportDto getMonthlyReport(Long userId, int year, int month) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        int totalPages = readingLogRepository.sumPagesByUserAndPeriod(user, startOfMonth, endOfMonth);
        int totalMinutes = readingLogRepository.sumMinutesByUserAndPeriod(user, startOfMonth, endOfMonth);
        long completedReviews = reviewRepository.countByUserAndCreatedAtBetween(user, startOfMonth, endOfMonth);
        
        return ReportDto.builder()
                .totalPages(totalPages)
                .totalMinutes(totalMinutes)
                .completedReviews(completedReviews)
                .build();
    }
    
    @Transactional(readOnly = true)
    public ReportDto getYearlyReport(Long userId, int year) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime endOfYear = startOfYear.plusYears(1).minusSeconds(1);

        int totalPages = readingLogRepository.sumPagesByUserAndPeriod(user, startOfYear, endOfYear);
        int totalMinutes = readingLogRepository.sumMinutesByUserAndPeriod(user, startOfYear, endOfYear);
        long completedReviews = reviewRepository.countByUserAndCreatedAtBetween(user, startOfYear, endOfYear);
        
        return ReportDto.builder()
                .totalPages(totalPages)
                .totalMinutes(totalMinutes)
                .completedReviews(completedReviews)
                .build();
    }
}
