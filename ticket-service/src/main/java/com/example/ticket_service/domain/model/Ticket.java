package com.example.ticket_service.domain.model;

import com.example.ticket_service.domain.exception.InvalidTicketException;
import com.example.ticket_service.domain.valueObject.ExpireDateVO;
import com.example.ticket_service.domain.valueObject.QRCodeVO;
import com.example.ticket_service.domain.valueObject.TicketIdVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Ticket {

    private TicketIdVO id;

    private QRCodeVO qRCode;

    private String room;

    private String seat;
    private String movie;

    private Boolean accessibility;

    private LocalDateTime movieTime;

    private ExpireDateVO expireTime;

    private Boolean valid;

    private Boolean inUse;

    public Ticket(
            TicketIdVO id, QRCodeVO qRCode,
            String room, String seat, String movie,
            Boolean accessibility, LocalDateTime movieTime,
            ExpireDateVO expireTime, Boolean valid, Boolean inUse
    ) {

        //movieTime.format(DateTimeFormatter.ofPattern("dd/MM:yyyy hh:mm:ss"));

        this.id = id;
        this.qRCode = qRCode;
        this.room = room;
        this.seat = seat;
        this.movie = movie;
        this.accessibility = accessibility;
        this.movieTime = movieTime;
        this.expireTime = expireTime;
        this.valid = valid;
        this.inUse = inUse;
    }

    public Ticket(
            QRCodeVO qRCode, String room,
            String seat, String movie,
            Boolean accessibility, LocalDateTime movieTime) {
        this.qRCode = qRCode;
        this.room = room;
        this.seat = seat;
        this.movie = movie;
        this.accessibility = accessibility;
        this.movieTime = movieTime;
        this.inUse = false;
    }

    public Boolean validateTicketExpiration(){
        return !this.expireTime.time().isAfter(LocalDateTime.now());
    }

    public void conciliate(){

        if(validateTicketExpiration()){
            throw new InvalidTicketException("Ticket expirado");
        }

        if(!this.valid){
            throw new InvalidTicketException("Ticket inválido");
        }

        this.valid = false;
        this.inUse = true;

    }

    public TicketIdVO getId() {
        return id;
    }

    public QRCodeVO getqRCode() {
        return qRCode;
    }

    public String getRoom() {
        return room;
    }

    public String getSeat() {
        return seat;
    }

    public String getMovie() {
        return movie;
    }

    public Boolean getAccessibility() {
        return accessibility;
    }

    public LocalDateTime getMovieTime() {
        return movieTime;
    }

    public ExpireDateVO getExpireTime() {
        return expireTime;
    }

    public void setqRCode(QRCodeVO qRCode) {
        this.qRCode = qRCode;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }
}
