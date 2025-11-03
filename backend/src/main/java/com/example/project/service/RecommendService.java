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

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final BookRecommendRepository bookRecommendRepository;
    private final GoalRecommendRepository goalRecommendRepository;

    // ✅ 유사도 높은 순 → 최신 날짜 순 정렬
    public Page<BookRecommendDto> getBookRecommendations(Long userId, int page, int size) {
        int offset = page * size;
        long total = bookRecommendRepository.countByUserId(userId);
    
        // ✅ 최신 날짜 → 유사도 높은 순으로 정렬
        List<BookRecommend> recs =
                bookRecommendRepository.findSortedRecommendationsByDate(userId, offset, size);
    
        Pageable pageable = PageRequest.of(page, size);
        Page<BookRecommend> sortedPage = new PageImpl<>(recs, pageable, total);
        return sortedPage.map(BookRecommendDto::fromEntity);
    }


    // ✅ 목표 추천 (가장 최신 1건)
    public GoalRecommendDto getLatestGoalRecommendation(Long userId) {
        GoalRecommend rec = goalRecommendRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("No goal recommendation found"));
        return GoalRecommendDto.fromEntity(rec);
    }
}
