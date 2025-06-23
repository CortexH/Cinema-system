package com.example.scheduling_service.application.dto.event;

import com.example.scheduling_service.application.dto.internal.SessionDTO;
import com.example.scheduling_service.domain.enums.SessionEventType;

import java.time.Instant;
import java.util.List;

public record SessionEndedEventDTO(
        Instant timestamp,
        List<SessionDTO> sessions
) {
}
