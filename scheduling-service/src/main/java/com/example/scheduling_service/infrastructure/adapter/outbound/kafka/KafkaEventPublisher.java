package com.example.scheduling_service.infrastructure.adapter.outbound.kafka;

import com.example.scheduling_service.application.dto.event.SessionBeginEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEndedEventDTO;
import com.example.scheduling_service.domain.port.out.SessionEventPublisherPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventPublisher implements SessionEventPublisherPort {



    @Value("kafka.topic.session.ended")
    private String sessionEndedTopic;

    @Override
    public void publishSessionsEnded(SessionEndedEventDTO event) {

    }

    @Override
    public void publishSessionsStarted(SessionBeginEventDTO eventDTO) {

    }

    @Override
    public void publishSessionsRemoved() {

    }

    @Override
    public void publishSessionsAdded() {

    }

    @Override
    public void publishSessionsEdited() {

    }

    @Override
    public void publishSessionNearToBegin() {

    }
}
