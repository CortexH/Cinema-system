package com.example.scheduling_service.application.port;

import com.example.scheduling_service.domain.domainEvents.SessionEvent;

public interface SessionEventUseCase {
    void handle(SessionEvent event);
}
