package com.example.room_service.infrastructure.adapter.outbound.persistence.mapper;

import com.example.room_service.domain.model.Seat;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.domain.valueObject.SeatIdVO;
import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.RoomEntity;
import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.SeatEntity;

import java.util.Objects;

public class SeatMapper {

    public static Seat toDomain(SeatEntity entity){
        if(entity == null){
            return null;
        }

        SeatIdVO seatId = SeatIdVO.from(entity.getId());
        RoomIdVO roomId = RoomIdVO.from(entity.getRoomEntity().getId());

        return new Seat(
                seatId, roomId, entity.getSeatNumber(),
                entity.getState(), entity.getBlocked()
        );
    }

    public static SeatEntity toEntity(Seat domain, RoomEntity roomEntity) {
        if (domain == null) {
            return null;
        }
        Objects.requireNonNull(roomEntity, "RoomEntity não pode ser nula ao mapear Seat");

        return new SeatEntity(
                domain.getSeatId().value(),
                domain.getSeatNumber(),
                domain.getAvailable(),
                domain.getInUse(),
                roomEntity
        );
    }

}
