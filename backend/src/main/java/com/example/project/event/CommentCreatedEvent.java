package com.example.project.events;

import lombok.Getter;

@Getter
public class CommentCreatedEvent {
    public enum TargetType { PERSONAL_POST, GROUP_POST, REVIEW }

    private final Long commentId;
    private final Long actorUserId;
    private final TargetType targetType;
    private final Long targetId;
    private final Long groupId;
    private final Long ownerUserId;

    public CommentCreatedEvent(Long commentId, Long actorUserId, TargetType targetType, Long targetId, Long groupId, Long ownerUserId) {
        this.commentId = commentId;
        this.actorUserId = actorUserId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.groupId = groupId;
        this.ownerUserId = ownerUserId;
    }
}
