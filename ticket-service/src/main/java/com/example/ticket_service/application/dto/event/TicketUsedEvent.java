package com.example.ticket_service.application.dto.event;

import java.time.LocalDateTime;

public record TicketUsedEvent(
        LocalDateTime timestamp,

        String room,
        String seat
) {
}
