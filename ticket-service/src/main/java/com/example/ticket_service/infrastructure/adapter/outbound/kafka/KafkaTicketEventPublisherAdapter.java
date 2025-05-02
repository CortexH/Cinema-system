package com.example.ticket_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.TicketRequestedEvent;
import com.example.ticket_service.application.dto.event.producer.TicketCreatedEvent;
import com.example.ticket_service.application.dto.event.producer.TicketCreationFailedEvent;
import com.example.ticket_service.application.dto.event.producer.TicketRequestedEventDTO;
import com.example.ticket_service.application.dto.event.producer.TicketUsedEvent;
import com.example.ticket_service.domain.port.out.TicketEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import br.com.cinemaSYS.events.TicketRequestedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTicketEventPublisherAdapter implements TicketEventPublisher {

    private final KafkaTemplate<String, TicketRequestedEvent> ticketRequestedTemplate;

    @Value("${kafka.topic.ticket.request}")
    private String ticketRequestedTopic;

    @Override
    public void publishTicketRequested(TicketRequestedEventDTO event) {
        try{
            TicketRequestedEvent avroEvent = TicketRequestedEvent.newBuilder()
                    .setRoom(event.room())
                    .setSeatNumbers(event.seat_numbers())
                    .build();


            log.info("Enviando evento Avro TicketRequestedEvent para sala: {}", avroEvent.getRoom());

            String key = avroEvent.getRoom();

            ticketRequestedTemplate.send(ticketRequestedTopic, key, avroEvent);

            log.info("avroEvent foi lançado :: {}", avroEvent.getRoom());

        } catch (Exception e) {
            log.error("FALHA AO REQUISITAR TICKET para sala: {}", event.room(), e);
        }
    }

    @Override
    public void publishTicketCreated(TicketCreatedEvent event) {

    }

    @Override
    public void publishTicketUsed(TicketUsedEvent event) {

    }

    @Override
    public void publishTicketCreationFail(TicketCreationFailedEvent event) {

    }
}
