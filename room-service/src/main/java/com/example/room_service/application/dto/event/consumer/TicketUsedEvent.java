package com.example.room_service.application.dto.event.consumer;

import java.time.Instant;

public record TicketUsedEvent(
        Instant timestamp,
        String room,
        String seat
) {

}
