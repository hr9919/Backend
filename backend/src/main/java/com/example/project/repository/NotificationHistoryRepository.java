// NEW
package com.example.project.repository;

import com.example.project.entity.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
    // 기본 save/find 등 사용
}
