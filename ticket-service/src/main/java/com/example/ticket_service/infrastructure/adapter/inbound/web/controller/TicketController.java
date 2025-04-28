package com.example.ticket_service.infrastructure.adapter.inbound.web.controller;

import com.example.ticket_service.application.dto.request.GenerateTicketRequest;
import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.port.in.TicketUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/conciliate/{ticketId}")
    public ResponseEntity<Void> conciliateTicket(
            @PathVariable("ticketId") String ticketId
    ){
        return null;
    }

}
