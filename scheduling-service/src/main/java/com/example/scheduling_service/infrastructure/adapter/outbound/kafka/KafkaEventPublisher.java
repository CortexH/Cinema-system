package com.example.scheduling_service.infrastructure.adapter.outbound.kafka;

import br.com.cinemaSYS.events.scheduler.SchedulerEvent;
import br.com.cinemaSYS.events.scheduler.SchedulerEventType;
import br.com.cinemaSYS.events.scheduler.SessionDTO;
import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.application.port.out.SessionEventPublisherPort;
import com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper.SchedulerEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

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
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                for (SessionEvent contractEvent : sessionEvents){
                    SchedulerEvent event = getEvent(contractEvent);
                    publishSessionEvent(event);
                }
            }
        });
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
