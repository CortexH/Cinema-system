package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.application.dto.event.TicketCreatedEvent;
import com.example.ticket_service.application.dto.event.TicketRequestedEvent;

public interface TicketEventPublisher {

    // lança o evento antes do pagamento ser realizado
    void publishTicketRequested(TicketRequestedEvent event);

    // lança o evento depois do pagamento ser realizado e ticket criado
    void publishTicketCreated(TicketCreatedEvent event);

    // lança o evento depois do ticket ser efetivamente usado
    void publishTicketUsed();

    // lança o evento se a criação do ticket falhar, por qualquer motivo.
    void publishTicketCreationFail();

}
