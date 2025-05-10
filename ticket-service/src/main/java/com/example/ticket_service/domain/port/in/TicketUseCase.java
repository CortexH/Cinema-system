package com.example.ticket_service.domain.port.in;

import com.example.ticket_service.application.dto.response.TicketConciliationResponse;
import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.valueObject.TicketIdVO;

import java.util.List;
import java.util.Optional;

public interface TicketUseCase {

    List<TicketResponse> purchaseTickets(String session, List<String> seatId, String paymentToken);
    TicketConciliationResponse conciliateTicket(String ticketId);

    Optional<Ticket> findTicketById(TicketIdVO ticketIdVO);

}
