package com.example.ticket_service.application.dto.event;

import java.time.LocalDateTime;

public record TicketCreationFailedEvent(
        String reason,
        LocalDateTime timestamp,
        String seat,
        String room
) {
}
