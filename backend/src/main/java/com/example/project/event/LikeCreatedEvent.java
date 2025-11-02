package com.example.project.events;

import lombok.Getter;

@Getter
public class LikeCreatedEvent {
    public enum TargetType { PERSONAL_POST, GROUP_POST, REVIEW }

    private final Long likeId;
    private final Long actorUserId;
    private final TargetType targetType;
    private final Long targetId;   // 개인 게시글/리뷰/그룹 게시글 id
    private final Long groupId;    // 그룹 게시글일 경우 필요, 없으면 null
    private final Long ownerUserId;

    public LikeCreatedEvent(Long likeId, Long actorUserId, TargetType targetType, Long targetId, Long groupId, Long ownerUserId) {
        this.likeId = likeId;
        this.actorUserId = actorUserId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.groupId = groupId;
        this.ownerUserId = ownerUserId;
    }
}
