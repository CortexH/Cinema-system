package com.example.scheduling_service.application.port.out;

import com.example.scheduling_service.application.dto.event.SessionBeginEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEndedEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEventAbstract;
import com.example.scheduling_service.domain.domainEvents.SessionEvent;

import java.util.List;

public interface SessionEventPublisherPort {

    void publishSessionEvent(SessionEvent event);
    void publishAll(List<SessionEvent> events);

}
