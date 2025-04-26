package com.example.room_service.application.dto.event;

import java.time.Instant;

public record SeatReleasedEvent(
        String roomId,
        String seatNumber,

        // String userId,

        Instant timestamp
) {
}
