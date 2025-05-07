package com.example.room_service.infrastructure.adapter.inbound.kafka;

import br.com.cinemaSYS.events.ticket.TicketCreatedEvent;
import br.com.cinemaSYS.events.ticket.TicketRequestedEvent;
import br.com.cinemaSYS.events.ticket.TicketUsedEvent;
import com.example.room_service.domain.port.in.RoomUseCase;
import com.example.room_service.domain.valueObject.RoomIdVO;
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
            clientIdPrefix = "ticket-request-consumer"
    )
    public void handleTicketRequested(TicketRequestedEvent event){
        roomUseCase.holdSeats(RoomIdVO.from(event.getRoomId()), event.getSeatNumbers());

    }

    @KafkaListener(
            topics = "${kafka.topic.ticket.created}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "ticket-creation-consumer"
    )
    public void handleTicketCreated(TicketCreatedEvent event){

        RoomIdVO roomIdVO = RoomIdVO.from(event.getRoomId());
        roomUseCase.reserve(roomIdVO, event.getSeats());
    }

    @KafkaListener(
            topics = "${kafka.topic.ticket.used}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "ticket-use-consumer"
    )
    public void handleTicketUsed(TicketUsedEvent event){

        RoomIdVO roomId = RoomIdVO.from(event.getRoom());
        roomUseCase.lockSeats(roomId, new ArrayList<>(List.of(event.getRoom())));

    }

}
