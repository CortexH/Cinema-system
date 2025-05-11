package com.example.room_service.infrastructure.adapter.inbound.kafka.listener;

import br.com.cinemaSYS.events.ticket.*;
import com.example.room_service.application.dto.event.consumer.TicketCreatedEvent;
import com.example.room_service.application.dto.event.consumer.TicketCreationFailedEvent;
import com.example.room_service.application.dto.event.consumer.TicketRequestedEvent;
import com.example.room_service.application.dto.event.consumer.TicketUsedEvent;
import com.example.room_service.domain.port.in.RoomUseCase;
import com.example.room_service.domain.valueObject.RoomIdVO;
import com.example.room_service.infrastructure.adapter.inbound.kafka.mapper.EventsDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TicketsEventsKafkaConsumer {

    private final RoomUseCase roomUseCase;

    @KafkaListener(
            topics = "${kafka.topic.ticket.request}",
            groupId = "${spring.kafka.consumer.group-id}",
            id = "ticketEventListener"
    )
    public void handleTicketEvent(TicketEvent event){

        switch (event.getEventType()){
            case TICKET_USED -> handleTicketUsed(EventsDTOMapper.toTicketUsedEvent(event));
            case TICKET_CREATED -> handleTicketCreated(EventsDTOMapper.toTicketCreatedEvent(event));
            case TICKET_REQUESTED -> handleTicketRequested(EventsDTOMapper.toTicketRequestedEvent(event));
            case TICKET_CREATION_FAILED -> handleTicketCreationFailed(EventsDTOMapper.toTicketCreationFailedEvent(event));

        }

    }

    public void handleTicketRequested(TicketRequestedEvent event){
        roomUseCase.holdSeats(RoomIdVO.from(event.roomId()), event.seats());
    }

    public void handleTicketCreated(TicketCreatedEvent event){

        RoomIdVO roomIdVO = RoomIdVO.from(event.roomId());
        roomUseCase.reserve(roomIdVO, event.seats());
    }

    public void handleTicketCreationFailed(TicketCreationFailedEvent event){
        RoomIdVO roomIdVO = RoomIdVO.from(event.room());
        roomUseCase.restartSeats(roomIdVO, event.seats());

    }

    public void handleTicketUsed(TicketUsedEvent event){
        //RoomIdVO roomId = RoomIdVO.from(event.room());
        //roomUseCase.lockSeats(roomId, new ArrayList<>(List.of(event.seat())));
    }

}
