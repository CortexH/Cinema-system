package com.example.scheduling_service.infrastructure.adapter.inbound.kafka;

import br.com.cinemaSYS.events.scheduler.SchedulerEvent;
import com.example.scheduling_service.application.port.SessionEventUseCase;
import com.example.scheduling_service.domain.domainEvents.SessionEvent;
import com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper.SchedulerEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEventConsumer {

    private final SessionEventUseCase eventUseCase;

    @KafkaListener(
            topics = "${kafka.topic.session-event}",
            groupId = "SysC-session-event-workers",
            id = "SchedulerEventListener"
    )
    public void handleSessionChangeEvent(SchedulerEvent schedulerEvent){
        System.out.println(schedulerEvent.toString());
        SessionEvent event = SchedulerEventMapper.toSessionEvent(schedulerEvent);
        eventUseCase.handle(event);
    }

}
