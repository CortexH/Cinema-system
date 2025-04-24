package com.example.room_service.infrastructure.adapter.inbound.web.mapper;

import com.example.room_service.application.dto.response.SeatResponse;
import com.example.room_service.domain.model.Seat;

public class SeatsDTOMapper {

    public static SeatResponse toResponse(Seat seat){
        if(seat == null) return null;

        return new SeatResponse(
                seat.getSeatId().value().toString(),
                seat.getSeatNumber(),
                seat.getAvailable());

    }

}
