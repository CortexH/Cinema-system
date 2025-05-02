package com.example.room_service.infrastructure.adapter.outbound.kafka;

import com.example.room_service.application.dto.event.RoomCreatedEvent;
import com.example.room_service.application.dto.event.SeatReleasedEvent;
import com.example.room_service.application.dto.event.SeatReservedEvent;
import com.example.room_service.domain.port.out.RoomEventPublisherPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRoomEventPublisherAdapter implements RoomEventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.room.created}")
    private String roomCreatedTopic;

    @Value("${kafka.topic.seat.reserved}")
    private String seatReservedTopic;

    @Value("${kafka.topic.seat.released}")
    private String seatReleasedTopic;

    @Override
    public void publishRoomCreated(RoomCreatedEvent event) {

        try{
            Message<RoomCreatedEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, roomCreatedTopic)
                    .setHeader(KafkaHeaders.KEY, event.roomId())
                    .build();

            kafkaTemplate.send(message);

        } catch (Exception e) {
            log.info("Falha ao criar sala :: {}", e.getMessage());
        }
    }

    @Override
    public void publishSeatReserved(SeatReservedEvent event) {
        try{

            String messageKey = event.roomId() + "-" + event.seatNumber();
            Message<SeatReservedEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, seatReservedTopic)
                    .setHeader(KafkaHeaders.KEY, messageKey)
                    .build();

            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.info("Falha ao reservar assento :: {}", e.getMessage());
        }

    }

    @Override
    public void publishSeatReleased(SeatReleasedEvent event) {
        try{
            String key = event.roomId() + "-" + event.seatNumber();

            Message<SeatReleasedEvent> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, seatReleasedTopic)
                    .setHeader(KafkaHeaders.KEY, key)
                    .build();

            kafkaTemplate.send(message);

        } catch (Exception e) {
            log.info("Falha ao liberar assento :: {}", e.getMessage());
        }
    }
}
