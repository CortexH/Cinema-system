package com.example.ticket_service.application.dto.event;

import java.util.List;

public record TicketRequestedEvent(
        String room,
        List<String> seat_numbers
) {
}
