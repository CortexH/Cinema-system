package com.example.ticket_service.infrastructure.adapter.inbound.web.controller;

import com.example.ticket_service.application.dto.request.GenerateTicketRequest;
import com.example.ticket_service.application.dto.response.TicketConciliationResponse;
import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.exception.TicketNotFoundException;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.port.in.TicketUseCase;
import com.example.ticket_service.domain.valueObject.TicketIdVO;
import com.example.ticket_service.infrastructure.adapter.inbound.web.mapper.TicketDTOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/tickets")
@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketUseCase ticketUseCase;

    @PostMapping
    public ResponseEntity<List<TicketResponse>> createTickets(
            @RequestBody @Valid GenerateTicketRequest request
            ){
        return new ResponseEntity<>(ticketUseCase.purchaseTickets(
                request.room(), request.seat_numbers(),
                request.payment_token()
        ), HttpStatus.OK);
    }

    @GetMapping("/conciliate/{ticketId}")
    public ResponseEntity<TicketConciliationResponse> conciliateTicket(
            @PathVariable("ticketId") String ticketId
    ){
        return new ResponseEntity<>(
                ticketUseCase.conciliateTicket(ticketId),
                HttpStatus.OK
        );
    }

    @GetMapping("/get/{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(
            @PathVariable("ticketId") String ticketId
    ){
        TicketIdVO ticketIdVO = TicketIdVO.from(ticketId);

        Optional<Ticket> ticket = ticketUseCase.findTicketById(ticketIdVO);

        return ticket.
                map(TicketDTOMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TicketNotFoundException("Ticket com ID especificado não encontrado"));
    }

}
