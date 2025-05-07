package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.application.dto.event.TicketCreatedEventDTO;
import com.example.ticket_service.application.dto.event.TicketCreationFailedEventDTO;
import com.example.ticket_service.application.dto.event.TicketRequestedEventDTO;
import com.example.ticket_service.application.dto.event.TicketUsedEventDTO;

public interface TicketEventPublisher {

    // lança o evento antes do pagamento ser realizado
    void publishTicketRequested(TicketRequestedEventDTO event);

    // lança o evento depois do pagamento ser realizado e ticket criado
    void publishTicketCreated(TicketCreatedEventDTO event);

    // lança o evento depois do ticket ser efetivamente usado
    void publishTicketUsed(TicketUsedEventDTO event);

    // lança o evento se a criação do ticket falhar, por qualquer motivo.
    void publishTicketCreationFail(TicketCreationFailedEventDTO event);

}
