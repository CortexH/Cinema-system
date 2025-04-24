package com.example.room_service.infrastructure.adapter.inbound.web.mapper;

import com.example.room_service.application.dto.response.RoomResponse;
import com.example.room_service.domain.model.Room;

public class RoomDTOMapper {

    public static RoomResponse toRoomResponse(Room room){

        if(room == null) return null;

        return new RoomResponse(
                room.getRoomId().value().toString(), room.getName(),
                room.getSeats().stream().map(SeatsDTOMapper::toResponse).toList()
        );
    }

}
