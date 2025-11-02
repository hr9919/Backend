package com.example.project.service;

import com.example.project.dto.BookRecommendDto;
import com.example.project.dto.GoalRecommendDto;
import com.example.project.entity.BookRecommend;
import com.example.project.entity.GoalRecommend;
import com.example.project.repository.BookRecommendRepository;
import com.example.project.repository.GoalRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final BookRecommendRepository bookRecommendRepository;
    private final GoalRecommendRepository goalRecommendRepository;

    // 책 추천 (최신순 + 페이징)
    public Page<BookRecommendDto> getBookRecommendations(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookRecommend> recs = bookRecommendRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return recs.map(BookRecommendDto::fromEntity);
    }

    // 목표 추천 (가장 최신 1건)
    public GoalRecommendDto getLatestGoalRecommendation(Long userId) {
        GoalRecommend rec = goalRecommendRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("No goal recommendation found"));
        return GoalRecommendDto.fromEntity(rec);
    }
}
