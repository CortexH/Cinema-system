package com.example.ticket_service.infrastructure.adapter.outbound.kafka;

import com.example.ticket_service.application.dto.event.TicketCreatedEvent;
import com.example.ticket_service.application.dto.event.TicketRequestedEvent;
import com.example.ticket_service.application.dto.event.TicketUsedEvent;
import com.example.ticket_service.domain.port.out.TicketEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkTicketEventPublisherAdapter implements TicketEventPublisher {

    @Override
    public void publishTicketRequested(TicketRequestedEvent event) {

    }

    @Override
    public void publishTicketCreated(TicketCreatedEvent event) {

    }

    @Override
    public void publishTicketUsed(TicketUsedEvent event) {

    }

    @Override
    public void publishTicketCreationFail() {

    }
}
