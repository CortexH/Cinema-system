package com.example.room_service.infrastructure.adapter.inbound.kafka.mapper;

import br.com.cinemaSYS.events.ticket.TicketEvent;
import com.example.room_service.application.dto.event.consumer.TicketCreatedEvent;
import com.example.room_service.application.dto.event.consumer.TicketCreationFailedEvent;
import com.example.room_service.application.dto.event.consumer.TicketRequestedEvent;
import com.example.room_service.application.dto.event.consumer.TicketUsedEvent;

public class EventsDTOMapper {

    public static TicketUsedEvent toTicketUsedEvent(TicketEvent event){
        return new TicketUsedEvent(event.getTimestamp(), event.getRoomId(), event.getSeats().getFirst());
    }

    public static TicketCreatedEvent toTicketCreatedEvent(TicketEvent event){
        return new TicketCreatedEvent(event.getRoomId(), event.getSeats(), event.getTimestamp());
    }

    public static TicketCreationFailedEvent toTicketCreationFailedEvent(TicketEvent event){
        return new TicketCreationFailedEvent(event.getReason(), event.getTimestamp(), event.getSeats(), event.getRoomId());
    }

    public static TicketRequestedEvent toTicketRequestedEvent(TicketEvent event){
        return new TicketRequestedEvent(event.getRoomId(), event.getSeats());
    }

}
