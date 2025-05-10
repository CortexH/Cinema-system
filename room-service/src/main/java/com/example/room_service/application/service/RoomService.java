package com.example.room_service.application.service;

import com.example.room_service.application.dto.event.publisher.RoomCreatedEventDTO;
import com.example.room_service.application.dto.event.publisher.SeatReleasedEventDTO;
import com.example.room_service.application.dto.event.publisher.SeatReservedEventDTO;
import com.example.room_service.domain.exception.RoomAlreadyExistsException;
import com.example.room_service.domain.exception.RoomNotFoundException;
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

        eventPublisherPort.publishRoomCreated(new RoomCreatedEventDTO(
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

    @Override
    public Optional<Room> findRoomByName(String name) {
        return repositoryPort.findRoomByName(name);
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
    public Boolean validateAllSeatsInARange(RoomIdVO roomId, List<String> seatIds) {
        Room room = repositoryPort.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o nome providenciado não encontrada"));

        return room.validateSeats(seatIds);
    }

    @Override
    public void restartSeats(RoomIdVO roomIdVO, List<String> seatIds) {

        Room room = repositoryPort.findById(roomIdVO)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o nome providenciado não encontrada"));

        room.resetSeats(seatIds);

        repositoryPort.save(room);
    }

    @Transactional
    @Override
    public void reserve(RoomIdVO roomIdVO, List<String> allSeats) {
        Objects.requireNonNull(roomIdVO, "o id da sala não pode ser nulo");
        Objects.requireNonNull(allSeats, "o id da poltrona não pode ser nulo");

        Room room = repositoryPort.findById(roomIdVO)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o id providenciado não encontrada"));


        allSeats.forEach(i -> {
            room.findSeatByNumber(i)
                    .orElseThrow(() -> new SeatNotFoundException("Poltrona com o número " + i + " não encontrada"));
        });

        room.reserveSeats(allSeats);

        repositoryPort.save(room);

        eventPublisherPort.publishSeatReserved(new SeatReservedEventDTO(
                allSeats, roomIdVO.value().toString(), Instant.now()
        ));

    }

    @Override
    @Transactional
    public void releaseSeat(RoomIdVO roomIdVO, List<String> allSeats) {
        Objects.requireNonNull(roomIdVO, "o id da sala não pode ser nulo");
        Objects.requireNonNull(allSeats, "o id da poltrona não pode ser nulo");

        Room room = repositoryPort.findById(roomIdVO)
                .orElseThrow(() -> new RoomNotFoundException("Sala com o id providenciado não encontrada"));


        allSeats.forEach(i -> {
            room.findSeatByNumber(i)
                    .orElseThrow(() -> new SeatNotFoundException("Poltrona com o número " + i + " não encontrada"));
        });

        room.releaseSeats(allSeats);

        repositoryPort.save(room);

        eventPublisherPort.publishSeatReleased(new SeatReleasedEventDTO(
                roomIdVO.value().toString(), allSeats, Instant.now()
        ));

    }

    @Transactional
    @Override
    public void holdSeats(RoomIdVO roomId, List<String> seatNumbers){
        Room room = repositoryPort.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Sala com id especificado não encontrado"));

         room.holdSeats(seatNumbers);

        repositoryPort.save(room);
    }

    @Override
    public void lockSeats(RoomIdVO roomId, List<String> seatNumbers) {

        Room room = repositoryPort.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Sala com id especificado não encontrado"));

        room.lockSeats(seatNumbers);

        repositoryPort.save(room);
    }

    @Override
    public void unlockSeats(RoomIdVO roomId, List<String> seatNumbers) {
        Room room = repositoryPort.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Sala com id especificado não encontrado"));

        room.unlockSeats(seatNumbers);

        repositoryPort.save(room);
    }

    @Transactional
    @Override
    public void deleteRoom(RoomIdVO roomIdVO) {
        repositoryPort.deleteById(roomIdVO);
    }

    @Transactional
    @Override
    public void deleteSeat(RoomIdVO roomId, List<String> seatNumber) {

        Room room = repositoryPort.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("A sala com id especificado não existe"));

        for(String num : seatNumber){
            room.deleteSeat(num);
        }

        repositoryPort.save(room);

    }


}
