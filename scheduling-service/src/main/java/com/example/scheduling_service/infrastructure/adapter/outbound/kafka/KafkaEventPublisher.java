package com.example.scheduling_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.scheduler.SchedulerEvent;
import com.example.scheduling_service.application.dto.event.SessionBeginEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEndedEventDTO;
import com.example.scheduling_service.domain.port.out.SessionEventPublisherPort;
import com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper.SessionEventDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaEventPublisher implements SessionEventPublisherPort {

    private final KafkaTemplate<String, SchedulerEvent> kafkaTemplate;

    @Value("kafka.topic.session.ended")
    private String sessionEndedTopic;

    @Override
    public void publishSessionsEnded(SessionEndedEventDTO dto) {
        try{

            SchedulerEvent event = SchedulerEvent.newBuilder()
                    .setSessions(new ArrayList<>(dto.sessions().stream().map(SessionEventDTOMapper::toDTO).toList()))
                    .build();

            String key = event.getTimestamp().toString();
            kafkaTemplate.send(sessionEndedTopic, key, event);

        } catch (Exception e) {
            log.info("Falha ao finalizar sessões :: {}", e.getMessage());
            throw e;
        }

    }

    @Override
    public void publishSessionsStarted(SessionBeginEventDTO dto) {

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
