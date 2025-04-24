package com.example.room_service.domain.model;

import com.example.room_service.domain.exception.SeatNotAvailableException;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.domain.valueObject.SeatIdVO;

import java.util.Objects;

public class Seat {

    private final SeatIdVO seatIdVO;
    private final RoomIdVO roomIdVO;
    private final String seatNumber;
    private Boolean available;

    public Seat(
            SeatIdVO id, RoomIdVO rId,
            String seatNum
    ){
        this.seatIdVO = Objects.requireNonNull(id, "'seatId' não pode ser nulo");
        this.roomIdVO = Objects.requireNonNull(rId, "'roomId' não pode ser nulo");
        this.seatNumber = Objects.requireNonNull(seatNum, "'seatNumber' não pode ser nulo");
        this.available = true;
    }

    public Seat(
            SeatIdVO id, RoomIdVO rId,
            String seatNum, Boolean available
    ){
        this.seatIdVO = Objects.requireNonNull(id, "'seatId' não pode ser nulo");
        this.roomIdVO = Objects.requireNonNull(rId, "'roomId' não pode ser nulo");
        this.seatNumber = Objects.requireNonNull(seatNum, "'seatNumber' não pode ser nulo");
        this.available = available;
    }

    public void reserve(){
        if(!this.available){
            throw new SeatNotAvailableException("assento não está disponível!");
        }
        this.available = false;
    }

    public void release(){
        this.available = true;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return Objects.equals(seatIdVO, seat.seatIdVO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatIdVO);
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatIdVO +
                ", roomId=" + roomIdVO +
                ", seatNumber='" + seatNumber + '\'' +
                ", available=" + available +
                '}';
    }

    public SeatIdVO getSeatId() {
        return seatIdVO;
    }

    public RoomIdVO getRoomId() {
        return roomIdVO;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
