package com.example.room_service.application.dto.event.consumer;

import java.util.List;

public record TicketRequestedEvent(
        String roomId,
        List<String> seats
) {
}
