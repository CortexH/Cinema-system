package com.example.scheduling_service.application.dto.internal;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessionDTO(
        UUID sessionId,
        UUID movieId,
        UUID roomId,
        LocalDateTime sessionBeginTime,
        LocalDateTime sessionEndTime,
        Duration movieDuration
) {
}
