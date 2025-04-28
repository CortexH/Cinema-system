package com.example.ticket_service.application.service;

import com.example.ticket_service.application.dto.event.TicketCreatedEvent;
import com.example.ticket_service.application.dto.event.TicketRequestedEvent;
import com.example.ticket_service.application.dto.internal.MovieInformationDTO;
import com.example.ticket_service.application.dto.internal.RealizePaymentDTO;
import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.exception.PaymentFailedException;
import com.example.ticket_service.domain.exception.SeatAlreadyReservedException;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.port.in.TicketUseCase;
import com.example.ticket_service.domain.port.out.MovieGateway;
import com.example.ticket_service.domain.port.out.PaymentGateway;
import com.example.ticket_service.domain.port.out.RoomGateway;
import com.example.ticket_service.domain.port.out.TicketEventPublisher;
import com.example.ticket_service.domain.valueObject.QRCodeVO;
import com.example.ticket_service.domain.valueObject.TicketIdVO;
import com.example.ticket_service.infrastructure.adapter.inbound.web.mapper.TicketDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketService implements TicketUseCase {

    private final TicketEventPublisher ticketEventPublisher;
    private final PaymentGateway paymentGateway;
    private final RoomGateway roomGateway;
    private final MovieGateway movieGateway;

    @Override
    public List<TicketResponse> purchaseTickets(String roomName, List<String> seatNumbers, String paymentToken) {

        TicketRequestedEvent ticketRequestedEvent = new TicketRequestedEvent(
                roomName, seatNumbers
        );

        ticketEventPublisher.publishTicketRequested(ticketRequestedEvent);

        if(!roomGateway.validateSeatReserveInBatch(roomName, seatNumbers)){
            // adicionar lançamento do evento de falha na criação do 'ticket'
            throw new SeatAlreadyReservedException("Uma das poltronas já está reservada");
        }

        if(!paymentGateway.realizePayment(new RealizePaymentDTO(paymentToken))){
            // adicionar lançamento do evento de falha na criação do 'ticket'
            throw new PaymentFailedException("Falha no pagamento");
        }

        MovieInformationDTO movieInfo = movieGateway.getMovieDataFromRoom(roomName);

        List<Ticket> requestedTickets = new ArrayList<>();

        for(String seat : seatNumbers){

            Ticket ticket = new Ticket(
                    TicketIdVO.generate(), new QRCodeVO(null),
                    roomName, seat, movieInfo.name(), movieInfo.accessibility(),
                    movieInfo.time()
            );

            requestedTickets.add(ticket);
        }

        TicketCreatedEvent ticketCreatedEvent = new TicketCreatedEvent(roomName, seatNumbers);
        ticketEventPublisher.publishTicketCreated(ticketCreatedEvent);

        return requestedTickets.stream()
                .map(TicketDTOMapper::toResponse)
                .toList();
    }

    @Override
    public void conciliateTicket(String ticketId) {

    }

}
