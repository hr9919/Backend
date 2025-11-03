package com.example.project.service;

import com.example.project.dto.TokenRequestBody;
import com.example.project.entity.UserFcmToken;
import com.example.project.repository.UserFcmTokenRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserFcmTokenService {

    private final UserFcmTokenRepository repository;
    private final FirebaseApp firebaseApp; // ✅ FCM용

    // ✅ FirebaseApp 생성자 주입 추가
    public UserFcmTokenService(UserFcmTokenRepository repository, FirebaseApp firebaseApp) {
        this.repository = repository;
        this.firebaseApp = firebaseApp;
    }

    public void saveOrUpdateToken(Long userId, TokenRequestBody body) {
        repository.findByUserIdAndToken(userId, body.getToken())
                .ifPresentOrElse(
                        token -> token.setLastSeen(LocalDateTime.now()),
                        () -> {
                            UserFcmToken newToken = new UserFcmToken();
                            newToken.setUserId(userId);
                            newToken.setToken(body.getToken());
                            newToken.setDeviceInfo(body.getDeviceInfo());
                            newToken.setCreatedAt(LocalDateTime.now());
                            newToken.setLastSeen(LocalDateTime.now());
                            repository.save(newToken);
                        }
                );
    }

    // ✅ FCM 테스트용 알림 전송
    public void sendTestNotification(String fcmToken) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle("테스트 알림")
                        .setBody("FCM 연동 확인")
                        .build())
                .build();
        try {
            String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
            System.out.println("Sent message ID: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
