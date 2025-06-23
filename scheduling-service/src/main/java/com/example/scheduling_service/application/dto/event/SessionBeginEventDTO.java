package com.example.scheduling_service.application.dto.event;

import com.example.scheduling_service.application.dto.internal.SessionDTO;

import java.time.Instant;
import java.util.List;

public record SessionBeginEventDTO(
        Instant timestamp,
        List<SessionDTO> sessions
) {
}
