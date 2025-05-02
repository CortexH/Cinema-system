package com.example.room_service.infrastructure.adapter.inbound.kafka;

import br.com.cinemaSYS.events.TicketRequestedEvent;
import com.example.room_service.domain.port.in.RoomUseCase;
import com.example.room_service.domain.valueObject.RoomIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TicketsEventsKafkaConsumer {

    private final RoomUseCase roomUseCase;

    @KafkaListener(
            topics = "${kafka.topic.ticket.requested}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleTicketRequested(TicketRequestedEvent event){

        roomUseCase.holdSeats(RoomIdVO.from(event.getRoomId()), event.getSeatNumbers());

    }

}
