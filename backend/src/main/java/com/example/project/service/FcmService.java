package com.example.project.service;

import com.example.project.entity.NotificationHistory;
import com.example.project.repository.NotificationHistoryRepository;
import com.example.project.repository.UserFcmTokenRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

// Lombok 사용 시 필요
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseApp firebaseApp;
    private final UserFcmTokenRepository tokenRepository;
    

    /**
     * 다건 전송 (data-only 메시지)
     * @return true = 최소 1개 이상 성공
     */
    public boolean sendNotificationToTokens(Long userId, List<String> tokens, String title, String body, Map<String, String> extraData) {
        if (tokens == null || tokens.isEmpty()) return false;

        boolean anyDelivered = false;

        for (String token : tokens) {
            try {
                // data-only 메시지로 전송
                Map<String, String> data = extraData != null ? extraData : Map.of();
                data.put("title", title);
                data.put("body", body);

                Message message = Message.builder()
                        .setToken(token)
                        .putAllData(data)
                        .build();

                FirebaseMessaging.getInstance(firebaseApp).send(message);
                anyDelivered = true;

            } catch (FirebaseMessagingException e) {
                System.err.println("Failed to send notification to token: " + token + " reason: " + e.getMessage());
                // 실패 토큰 DB에서 삭제 가능
                try { tokenRepository.deleteByToken(token); } catch (Exception ex) { ex.printStackTrace(); }
            }
        }

        return anyDelivered;
    }

    /**
     * 단일 토큰 테스트 전송
     */
    public void sendSingleTokenNotification(String token, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .putAllData(Map.of("title", title, "body", body))
                    .build();

            FirebaseMessaging.getInstance(firebaseApp).send(message);
            System.out.println("✅ Sent test message to token: " + token);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ Failed to send test message: " + e.getMessage());
        }
    }
}