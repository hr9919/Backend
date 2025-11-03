package com.example.project.repository;

import com.example.project.entity.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, Long> {
    Optional<UserFcmToken> findByUserIdAndToken(Long userId, String token);
    // 개인 알림용
    List<UserFcmToken> findAllByUserId(Long userId);

    void deleteByToken(String token);

    // 그룹 알림: groupId로 해당 그룹에 속한 유저 토큰 조회
  @Query(value = "SELECT token FROM user_fcm_tokens WHERE user_id IN " +
               "(SELECT user_id FROM group_members WHERE group_id = :groupId)", 
       nativeQuery = true)
List<String> findTokensByGroupId(Long groupId);



}
