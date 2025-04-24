package com.example.room_service.application.dto.event;

import java.time.Instant;

public record SeatReservedEvent(
        String seatNumber,
        String roomId,

        // String userId

        Instant timestamp
) {
}
