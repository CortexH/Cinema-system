package com.example.room_service.domain.port.out;

import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.valueObject.RoomIdVO;

import java.util.List;
import java.util.Optional;

public interface RoomRepositoryPort {

    Room save(Room room);

    Optional<Room> findById(RoomIdVO id);
    List<Room> findAll();

    void deleteById(RoomIdVO id);

}
