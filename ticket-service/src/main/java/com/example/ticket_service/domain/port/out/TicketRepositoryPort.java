package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.valueObject.TicketIdVO;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryPort {

    Optional<Ticket> findTicketById(TicketIdVO id);

    Ticket saveTicket(Ticket ticket);
    List<Ticket> saveAllTickets(List<Ticket> tickets);

}
