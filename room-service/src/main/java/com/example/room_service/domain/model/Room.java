package com.example.room_service.domain.model;

import com.example.room_service.domain.exception.SeatNotFoundException;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.domain.valueObject.SeatIdVO;

import java.util.*;

public class Room {

    private final RoomIdVO roomIdVO;
    private final String name;
    private final List<Seat> seats;

    public Room(RoomIdVO roomIdVO, String name, Integer seatRows, Integer seatPerRow){
        this.roomIdVO = roomIdVO;
        this.name = name;
        this.seats = new ArrayList<>();
        initializeSeats(seatRows, seatPerRow);
    }

    public Room(RoomIdVO roomIdVO, String name, List<Seat> seats){
        this.roomIdVO = roomIdVO;
        this.name = name;
        this.seats = seats;
    }

    private void initializeSeats(int rows, int seatsPerRow) {
        if (rows <= 0 || seatsPerRow <= 0) {
            throw new IllegalArgumentException("Number of rows and seats per row must be positive.");
        }
        for (char row = 'A'; row < 'A' + rows; row++) {
            for (int number = 1; number <= seatsPerRow; number++) {
                String seatNumber = "" + row + number;
                this.seats.add(new Seat(SeatIdVO.generate(), this.roomIdVO, seatNumber));
            }
        }
    }

    public Optional<Seat> findSeatByNumber(String number){
        return seats.stream().filter(
                i -> i.getSeatNumber().equals(number)
        ).findFirst();
    }

    public Seat reserveSeat(String number){
        Seat seat = findSeatByNumber(number)
                .orElseThrow(() -> new SeatNotFoundException("Poltrona com o id especificado não foi encontrada"));

        seat.reserve();

        return seat;
    }

    public Seat releaseSeat(String number){
        Seat seat = findSeatByNumber(number)
                .orElseThrow(() -> new SeatNotFoundException("Poltrona com o id especificado não foi encontrada"));

        seat.release();
        return seat;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomIdVO, room.roomIdVO);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomIdVO);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomIdVO +
                ", name='" + name + '\'' +
                ", numberOfSeats=" + seats.size() +
                '}';
    }

    public RoomIdVO getRoomId() {
        return roomIdVO;
    }

    public String getName() {
        return name;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}
