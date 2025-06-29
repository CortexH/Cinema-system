package com.example.scheduling_service.domain.domainEvents;

import com.example.scheduling_service.domain.enums.SessionEventType;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public interface SessionEvent {

    SessionEventType eventType();
    Instant timestamp();

}
