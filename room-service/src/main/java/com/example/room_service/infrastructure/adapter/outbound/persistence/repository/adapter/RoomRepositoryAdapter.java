package com.example.room_service.infrastructure.adapter.outbound.persistence.repository.adapter;

import com.example.room_service.domain.exception.RoomNotFoundException;
import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.model.Seat;
import com.example.room_service.domain.port.out.RoomRepositoryPort;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.RoomEntity;
import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.SeatEntity;
import com.example.room_service.infrastructure.adapter.outbound.persistence.mapper.RoomMapper;
import com.example.room_service.infrastructure.adapter.outbound.persistence.mapper.SeatMapper;
import com.example.room_service.infrastructure.adapter.outbound.persistence.repository.repository.SpringRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomRepositoryAdapter implements RoomRepositoryPort {

    private final SpringRoomRepository roomRepository;

    @Override
    public Room save(Room room) {
        return RoomMapper.toDomain(
                roomRepository.save(RoomMapper.toEntity(room))
        );
    }

    @Override
    public Optional<Room> findRoomByName(String name) {
        Optional<RoomEntity> roomEntity = roomRepository.findByName(name);
        return roomEntity.map(RoomMapper::toDomain);
    }

    @Override
    public Optional<Room> findById(RoomIdVO id) {
        Optional<RoomEntity> roomEntity = roomRepository.findById(id.value());
        return roomEntity.map(RoomMapper::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll().stream().map(RoomMapper::toDomain).toList();
    }

    @Override
    public List<Seat> findAllSeatsInRange(String roomName, List<String> seats) {
        return roomRepository.findByName(roomName)
                .orElseThrow(() -> new RoomNotFoundException("Sala com id especificado não encontrada."))
                .getSeats().stream().filter(i -> seats.contains(i.getSeatNumber()))
                .map(SeatMapper::toDomain).toList();
    }

    @Override
    public void deleteById(RoomIdVO id) {
        roomRepository.deleteById(id.value());
    }

}
