package com.example.room_service.application.dto.event.publisher;

import java.time.Instant;
import java.util.List;

public record SeatReservedEventDTO(
        List<String> seatNumber,
        String roomId,
        Instant timestamp
) {
}
