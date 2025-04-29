package com.example.ticket_service.infrastructure.adapter.outbound.persistence.repository.adapter;

import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.port.out.TicketRepositoryPort;
import com.example.ticket_service.infrastructure.adapter.outbound.persistence.mapper.TicketMapper;
import com.example.ticket_service.infrastructure.adapter.outbound.persistence.repository.repository.SpringTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TicketRepositoryAdapter implements TicketRepositoryPort {

    private final SpringTicketRepository repository;

    @Override
    public Ticket findTicketById(String id) {
        return null;
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return TicketMapper.toDomain(repository.save(TicketMapper.toBase(ticket)));
    }

    @Override
    public List<Ticket> saveAllTickets(List<Ticket> tickets) {
        return repository.saveAll(tickets.stream()
                .map(TicketMapper::toBase)
                .toList()).stream().map(TicketMapper::toDomain).toList();
    }

}
