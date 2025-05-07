package com.example.ticket_service.application.dto.event;

import java.time.Instant;
import java.time.LocalDateTime;

public record TicketUsedEventDTO(

        Instant timestamp,
        String roomId,
        String seat
) {
}
