/*package com.example.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {
    public void pushToUser(Long userId, String title, String body) {
        // TODO: Firebase Cloud Messaging 연동 지점
        log.info("PUSH -> userId: {}, title: {}, body: {}", userId, title, body);
    }
}*/
// NotificationService.java
// NotificationService.java
// NotificationService.java
package com.example.project.service;

import com.example.project.entity.NotificationHistory;
import com.example.project.entity.UserFcmToken;
import com.example.project.repository.NotificationHistoryRepository;
import com.example.project.repository.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// 아래 두 개는 Lombok 사용 시 필요
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FcmService fcmService;
    private final UserFcmTokenRepository tokenRepository;
    private final NotificationHistoryRepository historyRepository;

    /**
     * 사용자에게 알림 전송 + 히스토리 기록
     */
    public void pushToUser(Long userId, String title, String body) {
        try {
            log.info("PUSH -> userId: {}, title: {}, body: {}", userId, title, body);

            List<UserFcmToken> tokens = tokenRepository.findAllByUserId(userId);
            List<String> tokenStrings = tokens.stream()
                    .map(UserFcmToken::getToken)
                    .collect(Collectors.toList());

if (tokenStrings.isEmpty()) {
                log.info("No FCM tokens found for userId={}", userId);
                return;
            }
            
            Map<String, String> data = new HashMap<>();
            data.put("type", "generic");
            data.put("title", title);
            data.put("body", body);

            boolean delivered = false;
            if (!tokenStrings.isEmpty()) {
                // FCM 전송
                delivered = fcmService.sendNotificationToTokens(userId, tokenStrings, title, body, data);
            } else {
                log.info("No FCM tokens found for userId={}", userId);
            }

            // 히스토리 기록
            saveHistory(userId, "generic", null, data, delivered);

        } catch (Exception e) {
            log.error("pushToUser failed for userId={} : {}", userId, e.getMessage(), e);
            saveHistory(userId, "generic", null, Map.of("title", title, "body", body), false);
        }
    }

    /**
     * NotificationHistory 저장
     */
    private void saveHistory(Long userId, String type, Long sourceId, Map<String, String> payload, boolean delivered) {
        try {
            NotificationHistory h = new NotificationHistory();
            h.setUserId(userId);
            h.setType(type);
            h.setSourceId(sourceId);
            h.setPayload(mapToJson(payload));
            h.setSentAt(LocalDateTime.now());
            h.setDelivered(delivered);
            historyRepository.save(h);
        } catch (Exception e) {
            log.warn("Failed to save notification history: {}", e.getMessage(), e);
        }
    }

    /**
     * Map<String,String> → JSON String
     */
    private String mapToJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> e : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(escapeJson(e.getKey())).append("\":\"").append(escapeJson(e.getValue())).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * JSON escape 처리
     */
    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
