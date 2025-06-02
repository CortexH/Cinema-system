package com.example.scheduling_service.application.dto.request;

import java.time.LocalDateTime;

public record SessionRequestDTO(
        String movieName,
        String roomId,
        LocalDateTime sessionBeginTime,
        LocalDateTime sessionEndTime
) {
}
