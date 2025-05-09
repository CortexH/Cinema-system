package com.example.room_service.domain.model;

import com.example.room_service.domain.enums.SeatState;
import com.example.room_service.domain.exception.SeatNotAvailableException;
import com.example.room_service.domain.exception.SeatNotFoundException;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.domain.valueObject.SeatIdVO;

import java.util.*;

public class Room {

    private final RoomIdVO roomIdVO;
    private final String name;
    private List<Seat> seats;

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

    public List<Seat> reserveSeats(List<String> seats){

        List<Seat> alteredSeats = new ArrayList<>();

        seats.forEach(i -> {

            Seat seat = findSeatByNumber(i)
                    .orElseThrow(() -> new SeatNotFoundException("Poltrona com número " + i + " não encontrada"));

            if(seats.contains(seat.getSeatNumber())){

                if(seat.getAvailable().equals(SeatState.BLOCKED)){
                    throw new SeatNotAvailableException("Poltrona com número " + i + " já está reservada");
                }
                seat.setAvailable(SeatState.BLOCKED);
                alteredSeats.add(seat);
            }
        });

        return alteredSeats;

    }

    public List<Seat> releaseSeats(List<String> seats){
        List<Seat> alteredSeats = new ArrayList<>();

        seats.forEach(i -> {

            Seat seat = findSeatByNumber(i)
                    .orElseThrow(() -> new SeatNotFoundException("Poltrona com número " + i + " não encontrada"));

            if(seats.contains(seat.getSeatNumber())){

                if(seat.getAvailable().equals(SeatState.FREE)){
                    throw new SeatNotAvailableException("Poltrona com número " + i + " já está livre");
                }
                seat.setAvailable(SeatState.FREE);
                alteredSeats.add(seat);
            }
        });

        return alteredSeats;

    }

    public List<Seat> holdSeats(List<String> seatNumbers){

        List<Seat> usedSeats = new ArrayList<>();

        this.seats.forEach(i -> {
            if(!seatNumbers.contains(i.getSeatNumber())){
                return;
            }
            if(i.getInUse() || !i.getAvailable().equals(SeatState.FREE)){
                throw new SeatNotAvailableException(("A poltrona de número " + i.getSeatNumber() + " não está liberada"));
            }
            i.setAvailable(SeatState.HOLD);
            usedSeats.add(i);
        });

        return usedSeats;

    }

    public List<Seat> lockSeats(List<String> seatNumbers){

        List<Seat> usedSeats = new ArrayList<>();

        this.seats.forEach(i -> {
            if(!seatNumbers.contains(i.getSeatNumber())){
                return;
            }
            if(i.getInUse() || !i.getAvailable().equals(SeatState.BLOCKED)){
                throw new SeatNotAvailableException(("A poltrona de número " + i.getSeatNumber() + " não está liberada"));
            }
            i.setInUse(true);
            usedSeats.add(i);
        });

        return usedSeats;
    }

    public List<Seat> unlockSeats(List<String> seatNumbers){

        List<Seat> usedSeats = new ArrayList<>();

        this.seats.forEach(i -> {
            if(!seatNumbers.contains(i.getSeatNumber())){
                return;
            }
            if(!i.getInUse()){
                throw new SeatNotAvailableException(("A poltrona de número " + i.getSeatNumber() + " não está liberada"));
            }
            i.setInUse(false);
            usedSeats.add(i);
        });

        return usedSeats;
    }

    public void deleteSeat(String number){
        Seat seat = seats.stream().filter(i -> i.getSeatNumber().equals(number))
                .findFirst()
                .orElse(null);

        if(seat == null){
            throw new SeatNotFoundException("Poltrona com número especificado não encontrada");
        }
        this.seats.remove(seat);

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
