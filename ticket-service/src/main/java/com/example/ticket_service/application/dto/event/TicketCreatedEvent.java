package com.example.ticket_service.application.dto.event;

import java.util.List;

public record TicketCreatedEvent(
        String room,
        List<String> seat_number
) {
}
