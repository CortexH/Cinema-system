package com.example.room_service.application.dto.response;

import java.util.List;

public record RoomResponse(
        String id,
        String name,
        List<SeatResponse> seats
) {
}
