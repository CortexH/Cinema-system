package com.example.room_service.application.dto.event;

import java.time.Instant;

public record RoomCreatedEventDTO(
        String roomId,
        String roomName,
        int totalSeats,

        Instant timestamp

) {
}
