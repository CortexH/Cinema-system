package com.example.room_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.room.RoomEvent;
import br.com.cinemaSYS.events.room.RoomEventType;
import com.example.room_service.application.dto.event.publisher.RoomCreatedEventDTO;
import com.example.room_service.application.dto.event.publisher.SeatReleasedEventDTO;
import com.example.room_service.application.dto.event.publisher.SeatReservedEventDTO;
import com.example.room_service.domain.port.out.RoomEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRoomEventPublisherAdapter implements RoomEventPublisherPort {

    private final KafkaTemplate<String, RoomEvent> kafkaTemplate;

    @Value("${kafka.topic.room.created}")
    private String roomCreatedTopic;

    @Value("${kafka.topic.seat.reserved}")
    private String seatReservedTopic;

    @Value("${kafka.topic.seat.released}")
    private String seatReleasedTopic;

    @Override
    public void publishRoomCreated(RoomCreatedEventDTO event) {

        try{

            RoomEvent avroEvent = RoomEvent.newBuilder()
                    .setRoomId(event.roomId())
                    .setRoomName(event.roomName())
                    .setTimestamp(Instant.now())
                    .setEventType(RoomEventType.ROOM_CREATED)
                    .setSeats(new ArrayList<>(event.seats()))
                    .build();

            String key = avroEvent.getRoomId();

            kafkaTemplate.send(roomCreatedTopic, key, avroEvent);
        } catch (Exception e) {
            log.info("Falha ao criar sala :: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    public void publishSeatReserved(SeatReservedEventDTO event) {
        try{

            RoomEvent avroEvent = RoomEvent.newBuilder()
                    .setRoomId(event.roomId())
                    .setTimestamp(Instant.now())
                    .setSeats(event.seatNumber())
                    .setEventType(RoomEventType.SEAT_RESERVED)
                    .build();

            String key = avroEvent.getRoomId();

            kafkaTemplate.send(seatReservedTopic, key, avroEvent);

        } catch (Exception e) {
            log.info("Falha ao reservar assento :: {}", e.getMessage());
        }

    }

    @Override
    public void publishSeatReleased(SeatReleasedEventDTO event) {
        try{

            RoomEvent avroEvent = RoomEvent.newBuilder()
                    .setRoomId(event.roomId())
                    .setTimestamp(Instant.now())
                    .setSeats(event.seatNumbers())
                    .setEventType(RoomEventType.SEAT_RELEASED)
                    .build();

            String key = avroEvent.getRoomId();

            kafkaTemplate.send(seatReleasedTopic, key, avroEvent);

        } catch (Exception e) {
            log.info("Falha ao liberar assento :: {}", e.getMessage());
        }
    }
}
