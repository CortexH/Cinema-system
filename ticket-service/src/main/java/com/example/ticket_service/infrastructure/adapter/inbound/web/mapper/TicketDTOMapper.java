package com.example.ticket_service.infrastructure.adapter.inbound.web.mapper;

import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.valueObject.QRCodeVO;
import com.example.ticket_service.domain.valueObject.TicketIdVO;

public class TicketDTOMapper {

    public static TicketResponse toResponse(Ticket ticket){
        return new TicketResponse(
                ticket.getId().value(),
                ticket.getqRCode().code(), ticket.getMovie(),
                ticket.getMovieTime(), ticket.getRoom(),
                ticket.getSeat(), ticket.getAccessibility()
        );
    }

    public static Ticket toEntity(TicketResponse ticket){
        return new Ticket(
                TicketIdVO.from(ticket.id()), QRCodeVO.from(ticket.qrCode()),
                ticket.room(), ticket.seat(), ticket.movie(),
                ticket.accessibility(), ticket.movie_time()
        );
    }

}
