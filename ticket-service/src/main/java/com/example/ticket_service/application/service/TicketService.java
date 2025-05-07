package com.example.ticket_service.application.service;

import com.example.ticket_service.application.dto.event.TicketCreatedEventDTO;
import com.example.ticket_service.application.dto.event.TicketCreationFailedEventDTO;
import com.example.ticket_service.application.dto.event.TicketRequestedEventDTO;
import com.example.ticket_service.application.dto.event.TicketUsedEventDTO;
import com.example.ticket_service.application.dto.internal.MovieInformationDTO;
import com.example.ticket_service.application.dto.internal.RealizePaymentDTO;
import com.example.ticket_service.application.dto.response.TicketConciliationResponse;
import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.exception.ExpiredTicketException;
import com.example.ticket_service.domain.exception.PaymentFailedException;
import com.example.ticket_service.domain.exception.SeatAlreadyReservedException;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.port.in.TicketUseCase;
import com.example.ticket_service.domain.port.out.*;
import com.example.ticket_service.domain.valueObject.ExpireDateVO;
import com.example.ticket_service.domain.valueObject.QRCodeVO;
import com.example.ticket_service.domain.valueObject.TicketIdVO;
import com.example.ticket_service.infrastructure.adapter.inbound.web.mapper.TicketDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketService implements TicketUseCase {

    private final TicketEventPublisher ticketEventPublisher;
    private final TicketRepositoryPort ticketRepositoryPort;
    private final PaymentGateway paymentGateway;
    private final RoomGateway roomGateway;
    private final MovieGateway movieGateway;
    private final QRCodeGeneratorPort qrCodeGeneratorPort;

    @Value("${app.config.qrcode.base-url}")
    private String QRCODE_URL;

    @Override
    public List<TicketResponse> purchaseTickets(String roomId, List<String> seatNumbers, String paymentToken) {

        if(!roomGateway.validateSeatReserveInBatch(roomId, seatNumbers)){
            throw new SeatAlreadyReservedException("Uma das poltronas solicitadas já está reservada");
        }

        TicketRequestedEventDTO ticketRequestedEventDTO = new TicketRequestedEventDTO(
                roomId, seatNumbers
        );

        ticketEventPublisher.publishTicketRequested(ticketRequestedEventDTO);

        if(!paymentGateway.realizePayment(new RealizePaymentDTO(paymentToken))){

            TicketCreationFailedEventDTO failedEvent = new TicketCreationFailedEventDTO(
                    "Payment fail", LocalDateTime.now(),
                    seatNumbers, roomId
            );

            ticketEventPublisher.publishTicketCreationFail(failedEvent);

            throw new PaymentFailedException("Falha no pagamento");
        }

        MovieInformationDTO movieInfo = movieGateway.getMovieDataFromRoom(roomId);

        List<Ticket> requestedTickets = new ArrayList<>();

        for(String seat : seatNumbers){

            Ticket ticket = new Ticket(
                    TicketIdVO.generate(), null,
                    roomId, seat, movieInfo.name(), movieInfo.accessibility(),
                    movieInfo.time(), ExpireDateVO.generate(movieInfo.time()), true
            );

            String qr = qrCodeGeneratorPort.generateQR(QRCODE_URL + ticket.getId().value(), 300, 300);
            ticket.setqRCode(QRCodeVO.from(qr));

            requestedTickets.add(ticket);
        }

        ticketRepositoryPort.saveAllTickets(requestedTickets);

        TicketCreatedEventDTO ticketCreatedEventDTO = new TicketCreatedEventDTO(roomId, seatNumbers, Instant.now());
        ticketEventPublisher.publishTicketCreated(ticketCreatedEventDTO);

        return requestedTickets.stream()
                .map(TicketDTOMapper::toResponse)
                .toList();
    }

    @Override
    public TicketConciliationResponse conciliateTicket(String ticketId) {
        Ticket ticket = ticketRepositoryPort.findTicketById(ticketId);

        if(ticket.getExpireTime().time().isBefore(LocalDateTime.now())){
            throw new ExpiredTicketException("Ticket expirado");
        }

        if(!roomGateway.lockSeat(ticket.getRoom(), ticket.getSeat())){
            throw new ExpiredTicketException("Ticket expirado");
        }

        TicketUsedEventDTO event = new TicketUsedEventDTO(
                Instant.now(), ticket.getRoom(),
                ticket.getSeat()
        );


        ticket.setValid(false);
        ticketRepositoryPort.saveTicket(ticket);

        ticketEventPublisher.publishTicketUsed(event);

        return new TicketConciliationResponse(
                ticket.getSeat(), ticket.getRoom()
        );
    }

}
