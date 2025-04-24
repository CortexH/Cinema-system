package com.example.room_service.application.dto.event;

import lombok.Builder;

import java.time.Instant;

public record RoomCreatedEvent(
        String roomId,
        String roomName,
        int totalSeats,

        Instant timestamp

) {
}
