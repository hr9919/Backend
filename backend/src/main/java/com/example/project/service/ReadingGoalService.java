package com.example.project.service;

import com.example.project.dto.ReadingGoalDto;
import com.example.project.entity.ReadingGoal;
import com.example.project.entity.ReadingGoal.GoalType;
import com.example.project.entity.User;
import com.example.project.exception.UserNotFoundException;
import com.example.project.repository.ReadingGoalRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingGoalService {

    private final ReadingGoalRepository goalRepository;
    private final UserRepository userRepository;

    // 목표 생성
    @Transactional
    public ReadingGoalDto createGoal(ReadingGoalDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. ID=" + dto.getUserId()));

        ReadingGoal goal = ReadingGoal.builder()
                .user(user)
                .goalType(dto.getGoalType())
                .targetBooks(dto.getTargetBooks())
                .completedBooks(0)
                .targetReviews(dto.getTargetReviews())
                .completedReviews(0)
                .targetMinutes(dto.getTargetMinutes())
                .completedMinutes(0)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .year(dto.getStartDate().getYear())
                .month(dto.getGoalType() == GoalType.MONTHLY ? dto.getStartDate().getMonthValue() : null)
                .bookProgress(0)
                .reviewProgress(0)
                .timeProgress(0)
                .build();

        goal.updateProgress();
        return ReadingGoalDto.fromEntity(goalRepository.save(goal));
    }

    // 목표 조회
    @Transactional(readOnly = true)
    public ReadingGoalDto getGoal(Long id) {
        ReadingGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("목표를 찾을 수 없습니다. ID=" + id));
        return ReadingGoalDto.fromEntity(goal);
    }

    // 목표 수정
    @Transactional
    public ReadingGoalDto updateGoal(Long id, ReadingGoalDto dto) {
        ReadingGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("목표를 찾을 수 없습니다. ID=" + id));

        goal.setGoalType(dto.getGoalType());
        goal.setTargetBooks(dto.getTargetBooks());
        goal.setTargetReviews(dto.getTargetReviews());
        goal.setTargetMinutes(dto.getTargetMinutes());
        goal.setStartDate(dto.getStartDate());
        goal.setEndDate(dto.getEndDate());

        goal.setYear(dto.getStartDate().getYear());
        goal.setMonth(dto.getGoalType() == GoalType.MONTHLY ? dto.getStartDate().getMonthValue() : null);

        goal.updateProgress();
        return ReadingGoalDto.fromEntity(goalRepository.save(goal));
    }

    // 목표 삭제
    @Transactional
    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }

    // 책 완료 처리
    @Transactional
    public ReadingGoalDto completeBook(Long id) {
        ReadingGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("목표를 찾을 수 없습니다. ID=" + id));
        goal.completeBook();
        return ReadingGoalDto.fromEntity(goalRepository.save(goal));
    }

    // 감상문 완료 처리
    @Transactional
    public ReadingGoalDto completeReview(Long id) {
        ReadingGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("목표를 찾을 수 없습니다. ID=" + id));
        goal.completeReview();
        return ReadingGoalDto.fromEntity(goalRepository.save(goal));
    }

    // 독서 시간 추가
    @Transactional
    public ReadingGoalDto addReadingTime(Long id, int minutes) {
        ReadingGoal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("목표를 찾을 수 없습니다. ID=" + id));
        goal.addReadingTime(minutes);
        return ReadingGoalDto.fromEntity(goalRepository.save(goal));
    }

    // 사용자별 책 완료 처리
    @Transactional
    public void completeBookByUser(Long userId) {
        LocalDate today = LocalDate.now();
        List<ReadingGoal> goals = goalRepository.findByUserIdAndStartDateBeforeAndEndDateAfter(userId, today, today);
        goals.forEach(goal -> {
            goal.completeBook();
            goalRepository.save(goal);
        });
    }

    // 사용자별 리뷰 완료 처리
    @Transactional
    public void completeReviewByUser(Long userId) {
        LocalDate today = LocalDate.now();
        List<ReadingGoal> goals = goalRepository.findByUserIdAndStartDateBeforeAndEndDateAfter(userId, today, today);
        goals.forEach(goal -> {
            goal.completeReview();
            goalRepository.save(goal);
        });
    }

    // 사용자별 독서 시간 추가
    @Transactional
    public void addReadingTimeByUser(Long userId, int minutes) {
        LocalDate today = LocalDate.now();
        List<ReadingGoal> goals = goalRepository.findByUserIdAndStartDateBeforeAndEndDateAfter(userId, today, today);
        goals.forEach(goal -> {
            goal.addReadingTime(minutes);
            goalRepository.save(goal);
        });
    }
}
