package com.example.room_service.application.dto.response;

import com.example.room_service.domain.enums.SeatState;

public record SeatResponse(
        String id,
        String seat_number,
        SeatState state,
        Boolean in_use
) {
}
