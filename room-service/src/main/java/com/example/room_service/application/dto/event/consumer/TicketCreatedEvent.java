package com.example.room_service.application.dto.event.consumer;

import java.time.Instant;
import java.util.List;

public record TicketCreatedEvent(
        String roomId,
        List<String> seats,
        Instant timestamp
) {
}
