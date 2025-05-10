package com.example.room_service.infrastructure.adapter.inbound.web.controller;

import com.example.room_service.application.dto.request.CreateRoomRequestDTO;
import com.example.room_service.application.dto.response.RoomResponse;
import com.example.room_service.application.dto.response.SeatResponse;
import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.model.Seat;
import com.example.room_service.domain.port.in.RoomUseCase;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.infrastructure.adapter.inbound.web.mapper.RoomDTOMapper;
import com.example.room_service.infrastructure.adapter.inbound.web.mapper.SeatsDTOMapper;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomUseCase roomUseCase;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody @Valid CreateRoomRequestDTO data){

        Room createdRoom = roomUseCase.createRoom(
                data.name(),
                data.rows(),
                data.seats_per_rows()
        );

        return ResponseEntity.ok(RoomDTOMapper.toRoomResponse(createdRoom));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> findRoomById(@PathVariable String roomId){
        RoomIdVO id = RoomIdVO.from(roomId);

        Optional<Room> room = roomUseCase.findRoomById(id);

        return room
                .map(RoomDTOMapper::toRoomResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/find")
    public ResponseEntity<RoomResponse> findRoomByName(@PathParam("name") String name){

        Optional<Room> room = roomUseCase.findRoomByName(name);

        return room
                .map(RoomDTOMapper::toRoomResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll(){
        List<Room> rooms = roomUseCase.findAllRooms();

        List<RoomResponse> responses = rooms.stream()
                .map(RoomDTOMapper::toRoomResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{roomId}/seats")
    public ResponseEntity<List<SeatResponse>> getSeatsFromRoomById(@PathVariable String roomId){

        RoomIdVO id = RoomIdVO.from(roomId);

        List<Seat> seats = roomUseCase.findSeatsByRoom(id);

        List<SeatResponse> response = seats.stream()
                .map(SeatsDTOMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/seats/{seatNumber}")
    public ResponseEntity<SeatResponse> getSeatByRoomIdAndSeatNumber(@PathVariable String roomId, @PathVariable String seatNumber){

        RoomIdVO rID = RoomIdVO.from(roomId);

        Optional<Seat> seat = roomUseCase.findSeatInRoom(rID, seatNumber);

        return seat
                .map(SeatsDTOMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/{roomId}/seats/{seatNumber}/reserve")
    public ResponseEntity<Void> reserveSeat(@PathVariable String roomId, @PathVariable String seatNumber){

        RoomIdVO id = RoomIdVO.from(roomId);

        roomUseCase.reserve(id, List.of(seatNumber));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/seats/{seatNumber}/release")
    public ResponseEntity<Void> releaseSeat(@PathVariable String roomId, @PathVariable String seatNumber){

        RoomIdVO id = RoomIdVO.from(roomId);
        roomUseCase.releaseSeat(id, List.of(seatNumber));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/seat/{seatId}/lock")
    public ResponseEntity<Void> lockSeat(
            @PathVariable("roomId") String roomId,
            @PathVariable("seatId") String seatId
    ){

        RoomIdVO roomIdVO = RoomIdVO.from(roomId);

        roomUseCase.lockSeats(roomIdVO, List.of(seatId));
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/{roomId}/seat/{seatId}/unlock")
    public ResponseEntity<Void> unlockSeat(
            @PathVariable("roomId") String roomId,
            @PathVariable("seatId") String seatId
    ){

        RoomIdVO roomIdVO = RoomIdVO.from(roomId);
        roomUseCase.unlockSeats(roomIdVO, List.of(seatId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{roomId}/seat/validate")
    public ResponseEntity<Boolean> validateSeatsDisponibility(
            @PathVariable("roomId") String roomId,
            @RequestBody List<String> seats
    ) {

        RoomIdVO roomIdVO = RoomIdVO.from(roomId);

        return ResponseEntity.ok(roomUseCase.validateAllSeatsInARange(roomIdVO, seats));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable("roomId") String roomid
    ){

        RoomIdVO roomIdVO = RoomIdVO.from(roomid);

        roomUseCase.deleteRoom(roomIdVO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{roomId}/seats")
    public ResponseEntity<Void> deleteSeatsInRoom(
            @PathVariable("roomId") String roomId,
            @RequestBody String[] seatsList
    ){

        RoomIdVO roomIdVO = RoomIdVO.from(roomId);
        roomUseCase.deleteSeat(roomIdVO, Arrays.stream(seatsList).toList());
        return ResponseEntity.noContent().build();

    }

}
