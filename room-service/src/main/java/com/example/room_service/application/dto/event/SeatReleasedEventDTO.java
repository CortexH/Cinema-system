package com.example.room_service.application.dto.event;

import java.time.Instant;
import java.util.List;

public record SeatReleasedEventDTO(
        String roomId,
        List<String> seatNumbers,

        // String userId,

        Instant timestamp
) {
}
