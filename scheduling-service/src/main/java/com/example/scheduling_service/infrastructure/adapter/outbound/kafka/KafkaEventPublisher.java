package com.example.scheduling_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.scheduler.SchedulerEvent;
import br.com.cinemaSYS.events.scheduler.SchedulerEventType;
import br.com.cinemaSYS.events.scheduler.SessionDTO;
import com.example.scheduling_service.application.dto.event.SessionBeginEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEndedEventDTO;
import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.domain.port.out.SessionEventPublisherPort;
import com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper.SchedulerEventMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper.SessionEventDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaEventPublisher implements SessionEventPublisherPort {

    private final KafkaTemplate<String, SchedulerEvent> kafkaTemplate;

    @Value("kafka.topic.session-event")
    private String sessionEventTopic;

    private void publishSessionEvent(SchedulerEvent event) {
        try{
            String key = event.getSession().getSessionId();
            kafkaTemplate.send(sessionEventTopic, key, event);
        } catch (Exception e) {
            log.info("Falha ao finalizar sessões :: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void publishSessionEvent(SessionEvent event) {
        publishSessionEvent(getEvent(event));
    }

    @Override
    public void publishAll(List<SessionEvent> sessionEvents){
        for (SessionEvent contractEvent : sessionEvents){
            SchedulerEvent event = getEvent(contractEvent);
            publishSessionEvent(event);
        }
    }

    private SchedulerEvent getEvent(SessionEvent model){
        return switch (model) {
            case SessionBeginEvent event -> SchedulerEventMapper.fromSessionBegin(event);
            case SessionEndEvent event -> SchedulerEventMapper.fromSessionEnd(event);
            case SessionSetupBeginEvent event -> SchedulerEventMapper.fromSessionSetupBegin(event);
            case SessionScheduledEvent event -> SchedulerEventMapper.fromSessionScheduled(event);
            case SessionNearToBeginEvent event -> SchedulerEventMapper.fromSessionNearToBegin(event);
            default -> throw new IllegalArgumentException("Tipo de evento não mapeado: " + model.getClass());
        };
    }

}
