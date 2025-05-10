package com.example.room_service.domain.port.in;

import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.model.Seat;
import com.example.room_service.domain.valueObject.RoomIdVO;

import java.util.List;
import java.util.Optional;

public interface RoomUseCase {

    Room createRoom(String name, Integer rows, Integer seatsPerRows);

    Optional<Room> findRoomById(RoomIdVO id);
    Optional<Room> findRoomByName(String name);
    List<Room> findAllRooms();

    List<Seat> findSeatsByRoom(RoomIdVO roomIdVO);

    Optional<Seat> findSeatInRoom(RoomIdVO roomIdVO, String seatId);

    Boolean validateAllSeatsInARange(RoomIdVO roomId, List<String> seatIds);

    void restartSeats(RoomIdVO roomIdVO, List<String> seatIds);

    void deleteRoom(RoomIdVO roomIdVO);
    void deleteSeat(RoomIdVO roomId, List<String> seatNumber);

    void reserve(RoomIdVO roomIdVO, List<String> seatId);
    void releaseSeat(RoomIdVO roomIdVO, List<String> seatId);

    void holdSeats(RoomIdVO roomId, List<String> seatNumber);
    void lockSeats(RoomIdVO roomId, List<String> seatNumbers);
    void unlockSeats(RoomIdVO roomId, List<String> seatNumbers);

}
