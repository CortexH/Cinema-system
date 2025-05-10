package com.example.ticket_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.ticket.*;
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
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTicketEventPublisherAdapter implements TicketEventPublisher {

    private final KafkaTemplate<String, TicketEvent> ticketEventKafkaTemplate;

    @Value("${kafka.topic.ticket.request}")
    private String ticketRequestedTopic;

    @Override
    public void publishTicketRequested(TicketRequestedEventDTO event) {
        try{

            TicketEvent avroEvent = TicketEvent.newBuilder()
                    .setEventId(UUID.randomUUID().toString())
                    .setEventType(TicketEventType.TICKET_REQUESTED)
                    .setRoomId(event.roomId())
                    .setSeats(event.seat_numbers())
                    .setTimestamp(Instant.now())
                    .build();

            ticketEventKafkaTemplate.send(ticketRequestedTopic, event.roomId(), avroEvent);

            log.info("TICKET REQUISITADO :: {}", avroEvent);

        } catch (Exception e) {
            log.error("FALHA AO REQUISITAR TICKET para sala: {}", event.roomId(), e);
        }
    }

    @Override
    public void publishTicketCreated(TicketCreatedEventDTO event) {
        try{

            TicketEvent avroEvent = TicketEvent.newBuilder()
                    .setEventId(UUID.randomUUID().toString())
                    .setEventType(TicketEventType.TICKET_CREATED)
                    .setRoomId(event.roomId())
                    .setSeats(event.seats())
                    .setTimestamp(Instant.now())
                    .build();

            ticketEventKafkaTemplate.send(ticketRequestedTopic, avroEvent.getRoomId(), avroEvent);
            log.info("TICKET CRIADO!!!");

        } catch (Exception e) {
            log.info("FALHA AO CRIAR TICKET", e);
        }
    }

    @Override
    public void publishTicketCreationFail(TicketCreationFailedEventDTO event) {
        try{

            TicketEvent avroEvent = TicketEvent.newBuilder()
                    .setEventId(UUID.randomUUID().toString())
                    .setEventType(TicketEventType.TICKET_CREATION_FAILED)
                    .setRoomId(event.room())
                    .setSeats(event.seats())
                    .setTimestamp(Instant.now())
                    .build();

            ticketEventKafkaTemplate.send(ticketRequestedTopic, avroEvent.getRoomId(), avroEvent);

            log.info("TICKET FALHOU PUBLICADO :: {}", avroEvent);

        } catch (Exception e) {
            log.info("EVENTO DE FALHA DE CRIAÇÃO DE TICKET FALHOU :: ", e);
        }
    }


    @Override
    public void publishTicketUsed(TicketUsedEventDTO event) {
        try{

            TicketEvent avroEvent = TicketEvent.newBuilder()
                    .setEventId(UUID.randomUUID().toString())
                    .setEventType(TicketEventType.TICKET_USED)
                    .setRoomId(event.roomId())
                    .setSeats(List.of(event.seat()))
                    .setTimestamp(Instant.now())
                    .build();

            ticketEventKafkaTemplate.send(ticketRequestedTopic, avroEvent.getRoomId(), avroEvent);

            log.info("TICKET USADO COM SUCESSO :: {}", avroEvent);

        } catch (Exception e) {
            log.info("FALHA AO USAR TICKET", e);
        }
    }
}
