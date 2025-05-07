package com.example.room_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.room.RoomCreatedEvent;
import com.example.room_service.application.dto.event.RoomCreatedEventDTO;
import com.example.room_service.application.dto.event.SeatReleasedEventDTO;
import com.example.room_service.application.dto.event.SeatReservedEventDTO;
import com.example.room_service.domain.port.out.RoomEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRoomEventPublisherAdapter implements RoomEventPublisherPort {

    private final KafkaTemplate<String, RoomCreatedEvent> kafkaTemplate;

    @Value("${kafka.topic.room.created}")
    private String roomCreatedTopic;

    @Value("${kafka.topic.seat.reserved}")
    private String seatReservedTopic;

    @Value("${kafka.topic.seat.released}")
    private String seatReleasedTopic;

    @Override
    public void publishRoomCreated(RoomCreatedEventDTO event) {
        try{

            RoomCreatedEvent avroEvent = RoomCreatedEvent.newBuilder()
                    .setRoomId(event.roomId())
                    .setTimestamp(Instant.now())
                    .setRoomName(event.roomName())
                    .setTotalSeats(event.totalSeats())
                    .build();

            String key = avroEvent.getRoomId();

            kafkaTemplate.send(roomCreatedTopic, key, avroEvent);

        } catch (Exception e) {
            log.info("Falha ao criar sala :: {}", e.getMessage());
        }
    }

    @Override
    public void publishSeatReserved(SeatReservedEventDTO event) {
        try{



        } catch (Exception e) {
            log.info("Falha ao reservar assento :: {}", e.getMessage());
        }

    }

    @Override
    public void publishSeatReleased(SeatReleasedEventDTO event) {
        try{

        } catch (Exception e) {
            log.info("Falha ao liberar assento :: {}", e.getMessage());
        }
    }
}
