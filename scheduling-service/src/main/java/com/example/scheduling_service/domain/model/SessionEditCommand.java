package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.enums.CommandStatus;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.exception.SessionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessionEditCommand(
        UUID id, UUID relatedSessionId, UUID movieId,
        UUID roomId, LocalDateTime sessionBeginTime,
        LocalDateTime sessionEndTime, Duration setupTime,
        Duration movieDuration, SessionScheduleState sessionScheduleState,
        LocalDateTime processedAt, LocalDateTime createdAt,
        CommandStatus status
) { }
