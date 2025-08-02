package com.example.scheduling_service.domain.dtos;

import com.example.scheduling_service.domain.enums.SessionScheduleState;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public record SessionEditDTO(
        UUID movieId, UUID roomId,
        LocalDateTime sessionBeginTime,
        LocalDateTime sessionEndTime,
        Duration setupTime,
        Duration movieDuration,
        SessionScheduleState sessionScheduleState
) {
}
