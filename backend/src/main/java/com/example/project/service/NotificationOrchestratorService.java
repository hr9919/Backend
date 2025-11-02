// NotificationOrchestratorService.java
package com.example.project.service;

import com.example.project.events.CommentCreatedEvent;
import com.example.project.events.LikeCreatedEvent;
import com.example.project.events.PostCreatedEvent;
import com.example.project.repository.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationOrchestratorService {

    private final NotificationService notificationService;
    private final UserFcmTokenRepository tokenRepository;

    // 내 게시글 좋아요 알림
    public void handleLikeEvent(LikeCreatedEvent event) {
        sendNotificationForPostInteraction(event.getOwnerUserId(), event.getGroupId(),
                "좋아요 알림", "내 감상문에 좋아요가 달렸어요");
    }

    // 내 게시글 댓글 알림
    public void handleCommentEvent(CommentCreatedEvent event) {
        sendNotificationForPostInteraction(event.getOwnerUserId(), event.getGroupId(),
                "댓글 알림", "내 감상문에 댓글이 달렸어요.");
    }

    // 그룹 새 게시글 알림
    public void handlePostEvent(PostCreatedEvent event) {
        List<String> tokens = tokenRepository.findTokensByGroupId(event.getGroupId());
        tokens.forEach(token -> notificationService.pushToUser(null, "새 게시글 알림",
                "그룹에 새 게시글이 등록되었어요."));
    }

    // 공통: 그룹/개인 게시글 상호작용 알림
    private void sendNotificationForPostInteraction(Long ownerUserId, Long groupId,
                                                    String title, String body) {
        List<String> tokens;

        if (groupId == null) {
            // 개인 게시글 → 작성자만
            tokens = tokenRepository.findAllByUserId(ownerUserId)
                    .stream().map(t -> t.getToken())
                    .collect(Collectors.toList());
        } else {
            // 그룹 게시글 → 작성자 + 그룹 멤버
            List<String> groupTokens = tokenRepository.findTokensByGroupId(groupId);
            List<String> ownerTokens = tokenRepository.findAllByUserId(ownerUserId)
                    .stream().map(t -> t.getToken())
                    .collect(Collectors.toList());

            // 중복 제거
            groupTokens.forEach(t -> { if (!ownerTokens.contains(t)) ownerTokens.add(t); });
            tokens = ownerTokens;
        }

        tokens.forEach(token -> notificationService.pushToUser(ownerUserId, title, body));
    }
}
