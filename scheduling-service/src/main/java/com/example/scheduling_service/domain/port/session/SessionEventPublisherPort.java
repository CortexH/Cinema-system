package com.example.scheduling_service.domain.port.session;

import com.example.scheduling_service.domain.domainEvents.SessionEvent;

import java.util.List;

public interface SessionEventPublisherPort {

    void publishSessionEvent(SessionEvent event);
    void publishAll(List<SessionEvent> events);

}
