package com.example.ticket_service.application.dto.event.producer;

import java.util.List;

public record TicketRequestedEventDTO(
        String room,
        String roomId,
        List<String> seat_numbers
) {
}
