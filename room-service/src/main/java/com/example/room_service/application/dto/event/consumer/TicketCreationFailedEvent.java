package com.example.room_service.application.dto.event.consumer;

import java.time.Instant;
import java.util.List;

public record TicketCreationFailedEvent(
        String reason,
        Instant timestamp,
        List<String> seats,
        String room
) {
}
