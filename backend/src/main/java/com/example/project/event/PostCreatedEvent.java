package com.example.project.events;

import lombok.Getter;

@Getter
public class PostCreatedEvent {
    private final Long postId;
    private final Long actorUserId;
    private final Long groupId;

    public PostCreatedEvent(Long postId, Long actorUserId, Long groupId) {
        this.postId = postId;
        this.actorUserId = actorUserId;
        this.groupId = groupId;
    }
}
