package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.application.dto.event.producer.TicketCreatedEvent;
import com.example.ticket_service.application.dto.event.producer.TicketCreationFailedEvent;
import com.example.ticket_service.application.dto.event.producer.TicketRequestedEventDTO;
import com.example.ticket_service.application.dto.event.producer.TicketUsedEvent;

public interface TicketEventPublisher {

    // lança o evento antes do pagamento ser realizado
    void publishTicketRequested(TicketRequestedEventDTO event);

    // lança o evento depois do pagamento ser realizado e ticket criado
    void publishTicketCreated(TicketCreatedEvent event);

    // lança o evento depois do ticket ser efetivamente usado
    void publishTicketUsed(TicketUsedEvent event);

    // lança o evento se a criação do ticket falhar, por qualquer motivo.
    void publishTicketCreationFail(TicketCreationFailedEvent event);

}
