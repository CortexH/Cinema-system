package com.example.room_service.application.dto.event.publisher;

import java.time.Instant;
import java.util.List;

public record RoomCreatedEventDTO(
        String roomId,
        String roomName,
        List<String> seats,

        Instant timestamp

) {
}
