package com.example.room_service.infrastructure.adapter.outbound.persistence.mapper;

import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.model.Seat;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.RoomEntity;
import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.SeatEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {

    public static Room toDomain(RoomEntity entity){
        if(entity == null){
            return null;
        }

        RoomIdVO roomId = RoomIdVO.from(entity.getId());

        List<Seat> domainSeats = entity.getSeats() != null ?
                entity.getSeats().stream()
                        .map(SeatMapper::toDomain).toList()
                : Collections.emptyList();


        return new Room(roomId, entity.getName(), domainSeats);
    }

    public static RoomEntity toEntity(Room domain){
        if(domain == null){
            return null;
        }


        RoomEntity entity = RoomEntity.builder()
                .id(domain.getRoomId().value())
                .name(domain.getName())
                .build();

        if(domain.getSeats() != null){
            domain.getSeats().forEach(i -> {
                SeatEntity seat = SeatMapper.toEntity(i, entity);
                entity.addSeat(seat);
            });
        }

        return entity;

    }

}
