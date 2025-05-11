package com.example.room_service.domain.model;

import com.example.room_service.domain.enums.SeatState;
import com.example.room_service.domain.exception.SeatNotAvailableException;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.domain.valueObject.SeatIdVO;

import java.util.Objects;

public class Seat {

    private final SeatIdVO seatIdVO;
    private final RoomIdVO roomIdVO;
    private final String seatNumber;
    private SeatState state;
    private Boolean inUse;

    public Seat(
            SeatIdVO id, RoomIdVO rId,
            String seatNum
    ){
        this.seatIdVO = Objects.requireNonNull(id, "'seatId' não pode ser nulo");
        this.roomIdVO = Objects.requireNonNull(rId, "'roomId' não pode ser nulo");
        this.seatNumber = Objects.requireNonNull(seatNum, "'seatNumber' não pode ser nulo");
        this.state = SeatState.AVAILABLE;
        this.inUse = false;
    }

    public Seat(
            SeatIdVO id, RoomIdVO rId,
            String seatNum, SeatState state, Boolean inUse){
        this.seatIdVO = Objects.requireNonNull(id, "'seatId' não pode ser nulo");
        this.roomIdVO = Objects.requireNonNull(rId, "'roomId' não pode ser nulo");
        this.seatNumber = Objects.requireNonNull(seatNum, "'seatNumber' não pode ser nulo");
        this.state = state;
        this.inUse = inUse;

    }

    public void reserve(){
        if(this.state != SeatState.AVAILABLE){
            throw new SeatNotAvailableException("Poltrona não está disponível!");
        }
        this.state = SeatState.RESERVED;

    }

    public void release(){
        if(this.state != SeatState.RESERVED){
            throw new SeatNotAvailableException("Poltrona não está bloqueada!");
        }

        this.state = SeatState.AVAILABLE;
    }

    public void lock(){

        if(this.getInUse()){
            throw new SeatNotAvailableException(("A poltrona de número " + this.getSeatNumber() + " já está em uso"));
        }

        this.inUse = true;
    }

    public Seat unlock(){

        if(!this.getInUse()){
            throw new SeatNotAvailableException(("A poltrona de número " + this.getSeatNumber() + " não está liberada"));
        }

        this.setInUse(false);
        return this;
    }

    public Seat reset(){
        this.state = SeatState.AVAILABLE;
        this.inUse = false;

        return this;
    }

    public Boolean validate(){

        return this.getAvailable().equals(SeatState.AVAILABLE) && !this.getInUse();
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
        return Objects.hash(seatNumber);
    }



    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatIdVO +
                ", roomId=" + roomIdVO +
                ", seatNumber='" + seatNumber + '\'' +
                ", state=" + state +
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

    public SeatState getAvailable() {
        return state;
    }

    public void setAvailable(SeatState state) {
        this.state = state;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }
}
