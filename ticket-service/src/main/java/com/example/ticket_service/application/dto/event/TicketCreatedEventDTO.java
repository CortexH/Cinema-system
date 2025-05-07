package com.example.ticket_service.application.dto.event;

import java.time.Instant;
import java.util.List;

public record TicketCreatedEventDTO(
        String roomId,
        List<String> seats,
        Instant timestamp
) {
}
