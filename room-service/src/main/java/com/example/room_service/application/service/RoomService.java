package com.example.room_service.application.service;

import com.example.room_service.application.dto.event.RoomCreatedEvent;
import com.example.room_service.application.dto.event.SeatReleasedEvent;
import com.example.room_service.application.dto.event.SeatReservedEvent;
import com.example.room_service.domain.exception.RoomAlreadyExistsException;
import com.example.room_service.domain.exception.RoomNotFoundException;
import com.example.room_service.domain.exception.SeatNotAvailableException;
import com.example.room_service.domain.exception.SeatNotFoundException;
import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.model.Seat;
import com.example.room_service.domain.port.in.RoomUseCase;
import com.example.room_service.domain.port.out.RoomEventPublisherPort;
import com.example.room_service.domain.port.out.RoomRepositoryPort;
import com.example.room_service.domain.valueObject.RoomIdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements RoomUseCase {

    private final RoomRepositoryPort repositoryPort;
    private final RoomEventPublisherPort eventPublisherPort;

    @Override
    @Transactional
    public Room createRoom(String name, Integer rows, Integer seatsPerRows) {

        RoomIdVO id = RoomIdVO.generate();
        Room room = new Room(id, name, rows, seatsPerRows);

        if(repositoryPort.findRoomByName(name).isPresent())
            throw new RoomAlreadyExistsException("Já existe uma sala com o nome especificado.");

        Room savedRoom = repositoryPort.save(room);

        eventPublisherPort.publishRoomCreated(new RoomCreatedEvent(
                room.getRoomId().value().toString(), room.getName(),
                room.getSeats().size(), Instant.now()
        ));

        return savedRoom;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Room> findRoomById(RoomIdVO id) {
        Objects.requireNonNull(id, "'id' não pode ser nulo");
        return repositoryPort.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Room> findAllRooms() {
        return repositoryPort.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Seat> findSeatsByRoom(RoomIdVO roomIdVO) {
        return repositoryPort.findById(roomIdVO)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o id providenciado não encontrada"))
                .getSeats();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Seat> findSeatInRoom(RoomIdVO roomIdVO, String seatId) {
        Objects.requireNonNull(roomIdVO, "o id da sala não pode ser nulo");
        Objects.requireNonNull(seatId, "o id da poltrona não pode ser nulo");

        Optional<Room> opRoom = repositoryPort.findById(roomIdVO);

        if(opRoom.isEmpty()){
            throw new RoomNotFoundException("Sala com o id providenciado não encontrada");
        }

        return opRoom.get().findSeatByNumber(seatId);

    }

    @Override
    public Boolean validateAllSeatsInARange(String roomName, List<String> seatIds) {
        repositoryPort.findRoomByName(roomName)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o nome providenciado não encontrada"));

        List<Seat> seats = repositoryPort.findAllSeatsInRange(roomName, seatIds);

        seats.forEach(i -> {
            if(!i.getAvailable() || i.getInUse()){
                throw new SeatNotAvailableException("Poltrona não está liberada");
            }
        });

        return true;
    }

    @Transactional
    @Override
    public void reserve(RoomIdVO roomIdVO, String seatId) {
        Objects.requireNonNull(roomIdVO, "o id da sala não pode ser nulo");
        Objects.requireNonNull(seatId, "o id da poltrona não pode ser nulo");

        Room room = repositoryPort.findById(roomIdVO)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o id providenciado não encontrada"));

        Seat seat = room.findSeatByNumber(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Poltrona com o número providenciado não encontrada"));

        room.reserveSeat(seatId);

        repositoryPort.save(room);

        // talvez seja melhor inserir um DTO de evento aqui.
        eventPublisherPort.publishSeatReserved(new SeatReservedEvent(
                seat.getSeatNumber(), roomIdVO.value().toString(), Instant.now()
        ));

    }

    @Override
    @Transactional
    public void releaseSeat(RoomIdVO roomIdVO, String seatId) {
        Objects.requireNonNull(roomIdVO, "o id da sala não pode ser nulo");
        Objects.requireNonNull(seatId, "o id da poltrona não pode ser nulo");

        Room room = repositoryPort.findById(roomIdVO)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o id providenciado não encontrada"));

        Seat seat = room.findSeatByNumber(seatId)
                .orElseThrow(() -> new SeatNotFoundException("Poltrona com o número providenciado não encontrada"));

        room.releaseSeat(seatId);

        repositoryPort.save(room);

        // talvez seja melhor inserir um DTO de evento aqui.
        eventPublisherPort.publishSeatReleased(new SeatReleasedEvent(
                roomIdVO.value().toString(), seat.getSeatNumber(), Instant.now()
        ));

    }
}
