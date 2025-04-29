package com.example.ticket_service.infrastructure.adapter.outbound.persistence.mapper;

import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.valueObject.ExpireDateVO;
import com.example.ticket_service.domain.valueObject.QRCodeVO;
import com.example.ticket_service.domain.valueObject.TicketIdVO;
import com.example.ticket_service.infrastructure.adapter.outbound.persistence.entity.TicketEntity;

import java.time.LocalDateTime;

public class TicketMapper {

    public static Ticket toDomain(TicketEntity entity){
        return new Ticket(
                TicketIdVO.from(entity.getId()),null,
                entity.getRoomId(), entity.getSeatId(), entity.getMovie(),
                entity.getAccessibility(), entity.getMovieTime(),
                ExpireDateVO.from(entity.getExpireTime()), entity.getValid()
        );
    }

    public static TicketEntity toBase(Ticket entity){
        return TicketEntity.builder()
                .id(entity.getId().value())
                .movie(entity.getMovie())
                .seatId(entity.getSeat())
                .valid(entity.validateTicketExpiration())
                //.qrCode(entity.getqRCode().code())
                .accessibility(entity.getAccessibility())
                .expireTime(entity.getExpireTime().time())
                .roomId(entity.getRoom())
                .movieTime(entity.getMovieTime())
                .build();
    }

}
