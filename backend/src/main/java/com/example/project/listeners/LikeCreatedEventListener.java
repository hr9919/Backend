package com.example.project.listeners;

import com.example.project.events.LikeCreatedEvent;
import com.example.project.service.NotificationOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
@RequiredArgsConstructor
public class LikeCreatedEventListener {

    private final NotificationOrchestratorService orchestrator;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handle(LikeCreatedEvent event) {
        orchestrator.handleLikeEvent(event);
    }
}
