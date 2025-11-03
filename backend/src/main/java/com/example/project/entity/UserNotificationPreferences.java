// NEW
package com.example.project.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_notification_preferences")
public class UserNotificationPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private Boolean notifyLikeMyPost = true;
    private Boolean notifyCommentMyPost = true;
    private Boolean notifyGroupNewPost = true;
    private Boolean notifyGroupLikeComment = true;

    private LocalDateTime updatedAt;

    public UserNotificationPreferences() {}

    // getters / setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Boolean getNotifyLikeMyPost() { return notifyLikeMyPost; }
    public void setNotifyLikeMyPost(Boolean notifyLikeMyPost) { this.notifyLikeMyPost = notifyLikeMyPost; }
    public Boolean getNotifyCommentMyPost() { return notifyCommentMyPost; }
    public void setNotifyCommentMyPost(Boolean notifyCommentMyPost) { this.notifyCommentMyPost = notifyCommentMyPost; }
    public Boolean getNotifyGroupNewPost() { return notifyGroupNewPost; }
    public void setNotifyGroupNewPost(Boolean notifyGroupNewPost) { this.notifyGroupNewPost = notifyGroupNewPost; }
    public Boolean getNotifyGroupLikeComment() { return notifyGroupLikeComment; }
    public void setNotifyGroupLikeComment(Boolean notifyGroupLikeComment) { this.notifyGroupLikeComment = notifyGroupLikeComment; }
}
