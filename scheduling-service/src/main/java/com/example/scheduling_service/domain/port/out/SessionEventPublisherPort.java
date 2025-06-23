package com.example.scheduling_service.domain.port.out;

import com.example.scheduling_service.application.dto.event.SessionBeginEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEndedEventDTO;

public interface SessionEventPublisherPort {

    void publishSessionsEnded(SessionEndedEventDTO event);
    void publishSessionsStarted(SessionBeginEventDTO eventDTO);

    void publishSessionsRemoved();
    void publishSessionsAdded();

    void publishSessionsEdited();

    void publishSessionNearToBegin();

}
