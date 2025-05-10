package com.example.room_service.application.dto.event.publisher;

import java.time.Instant;

public record RoomCreatedEventDTO(
        String roomId,
        String roomName,
        int totalSeats,

        Instant timestamp

) {
}
