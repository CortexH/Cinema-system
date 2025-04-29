package com.example.ticket_service.domain.port.in;

import com.example.ticket_service.application.dto.response.TicketConciliationResponse;
import com.example.ticket_service.application.dto.response.TicketResponse;

import java.util.List;

public interface TicketUseCase {

    List<TicketResponse> purchaseTickets(String session, List<String> seatId, String paymentToken);
    TicketConciliationResponse conciliateTicket(String ticketId);

}
