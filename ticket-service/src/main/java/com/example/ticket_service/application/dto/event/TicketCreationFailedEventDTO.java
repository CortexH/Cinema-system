package com.example.ticket_service.application.dto.event;

import java.time.LocalDateTime;
import java.util.List;

public record TicketCreationFailedEventDTO(
        String reason,
        LocalDateTime timestamp,
        List<String> seats,
        String room
) {
}
