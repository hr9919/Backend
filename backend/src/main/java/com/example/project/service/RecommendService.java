// RecommendService.java
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final BookRecommendRepository bookRecommendRepository;
    private final GoalRecommendRepository goalRecommendRepository;

    // ✅ 날짜(일 단위) 내림차순 → 같은 날짜 안에서는 hybridScore 내림차순 → 마지막 동률 createdAt 내림차순
    public Page<BookRecommendDto> getBookRecommendations(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookRecommend> recs = bookRecommendRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        // 1) 원본 Page의 content를 복사본으로 생성 (정렬 안전)
        List<BookRecommend> content = new ArrayList<>(recs.getContent());

        // 2) 날짜(일) 기준 내림차순, 같은 날짜 안에서는 hybridScore 내림차순, 마지막 타이브레이커로 createdAt 내림차순
        content.sort(
            Comparator
                .comparing(
                    (BookRecommend r) -> r.getCreatedAt() != null ? r.getCreatedAt().toLocalDate() : LocalDate.MIN,
                    Comparator.reverseOrder()
                )
                .thenComparing(
                    (BookRecommend r) -> r.getHybridScore() != null ? r.getHybridScore() : 0.0,
                    Comparator.reverseOrder()
                )
                .thenComparing(
                    BookRecommend::getCreatedAt,
                    Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        Page<BookRecommend> sortedPage = new PageImpl<>(content, pageable, recs.getTotalElements());
        return sortedPage.map(BookRecommendDto::fromEntity);
    }

    // 목표 추천
    public GoalRecommendDto getLatestGoalRecommendation(Long userId) {
        GoalRecommend rec = goalRecommendRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            .orElseThrow(() -> new RuntimeException("No goal recommendation found"));
        return GoalRecommendDto.fromEntity(rec);
    }
}