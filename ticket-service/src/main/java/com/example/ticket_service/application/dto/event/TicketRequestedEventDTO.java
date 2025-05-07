package com.example.ticket_service.application.dto.event;

import java.util.List;

public record TicketRequestedEventDTO(
        String roomId,
        List<String> seat_numbers
) {
}
