package com.example.ticket_service.application.dto.event.producer;

import java.time.LocalDateTime;
import java.util.List;

public record TicketCreationFailedEvent(
        String reason,
        LocalDateTime timestamp,
        List<String> seat,
        String room
) {
}
