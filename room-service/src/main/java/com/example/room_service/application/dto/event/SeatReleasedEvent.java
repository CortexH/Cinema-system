package com.example.room_service.application.dto.event;

import java.time.Instant;

public record SeatReleasedEvent(
        String roomId,
        String seatId,

        // String userId,

        Instant timestamp
) {
}
