package com.example.ticket_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.ticket.TicketCreatedEvent;
import br.com.cinemaSYS.events.ticket.TicketRequestedEvent;
import br.com.cinemaSYS.events.ticket.TicketUsedEvent;
import com.example.ticket_service.application.dto.event.TicketCreatedEventDTO;
import com.example.ticket_service.application.dto.event.TicketCreationFailedEventDTO;
import com.example.ticket_service.application.dto.event.TicketRequestedEventDTO;
import com.example.ticket_service.application.dto.event.TicketUsedEventDTO;
import com.example.ticket_service.domain.port.out.TicketEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTicketEventPublisherAdapter implements TicketEventPublisher {

    private final KafkaTemplate<String, TicketRequestedEvent> ticketRequestedTemplate;
    private final KafkaTemplate<String, TicketCreatedEvent> ticketCreatedTemplate;
    private final KafkaTemplate<String, TicketUsedEvent> ticketUsedTemplate;

    @Value("${kafka.topic.ticket.request}")
    private String ticketRequestedTopic;

    @Value("${kafka.topic.ticket.created}")
    private String ticketCreatedTopic;

    @Value("${kafka.topic.ticket.used}")
    private String ticketUsedTopic;

    @Override
    public void publishTicketRequested(TicketRequestedEventDTO event) {
        try{
            TicketRequestedEvent avroEvent = TicketRequestedEvent.newBuilder()
                    .setRoomId(event.roomId())
                    .setSeatNumbers(event.seat_numbers())
                    .build();

            String key = avroEvent.getRoomId();

            ticketRequestedTemplate.send(ticketRequestedTopic, key, avroEvent);

        } catch (Exception e) {
            log.error("FALHA AO REQUISITAR TICKET para sala: {}", event.roomId(), e);
        }
    }

    @Override
    public void publishTicketCreated(TicketCreatedEventDTO event) {

        try{
            TicketCreatedEvent avroEvent = TicketCreatedEvent.newBuilder()
                    .setRoomId(event.roomId())
                    .setTimestamp(Instant.now())
                    .setSeats(event.seats())
                    .build();

            ticketCreatedTemplate.send(ticketCreatedTopic, avroEvent.getRoomId(), avroEvent);

            log.info("Ticket enviado");
        } catch (Exception e) {
            log.info("FALHA AO CRIAR TICKET", e);
        }
    }

    @Override
    public void publishTicketUsed(TicketUsedEventDTO event) {
        try{
            TicketUsedEvent avroEvent = TicketUsedEvent.newBuilder()
                    .setRoom(event.roomId())
                    .setSeat(event.seat())
                    .setTimestamp(Instant.now())
                    .build();

            ticketUsedTemplate.send(ticketUsedTopic, event.roomId(), avroEvent);

        } catch (Exception e) {
            log.info("FALHA AO USAR TICKET", e);
        }
    }

    @Override
    public void publishTicketCreationFail(TicketCreationFailedEventDTO event) {

    }
}
