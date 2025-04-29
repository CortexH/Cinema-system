package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.domain.model.Ticket;

import java.util.List;

public interface TicketRepositoryPort {

    Ticket findTicketById(String id);

    Ticket saveTicket(Ticket ticket);
    List<Ticket> saveAllTickets(List<Ticket> tickets);

}
