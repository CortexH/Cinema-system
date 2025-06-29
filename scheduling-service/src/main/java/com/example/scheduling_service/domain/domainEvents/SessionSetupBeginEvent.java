package com.example.scheduling_service.domain.domainEvents;

import com.example.scheduling_service.domain.enums.SessionEventType;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessionSetupBeginEvent(
        SessionEventType eventType, Instant timestamp,
        UUID sessionId,
        UUID movieId, UUID roomId,
        LocalDateTime sessionBeginTime,
        LocalDateTime sessionEndTime,
        Duration movieDuration
) implements SessionEvent{
}
