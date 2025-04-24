package com.example.room_service.application.dto.response;

public record SeatResponse(
        String id,
        String seat_number,
        Boolean available
) {
}
