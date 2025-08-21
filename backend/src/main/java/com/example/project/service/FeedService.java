package com.example.project.service;

import com.example.project.dto.FeedDto;
import com.example.project.entity.Feed;
import com.example.project.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public List<FeedDto> getFeed(Long userId, int page, int size) {
        return feedRepository.findByUser_Id(userId, PageRequest.of(page, size))
                .stream()
                .map(f -> FeedDto.builder()
                        .feedId(f.getId())
                        .content(f.getContent())
                        .createdAt(f.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
