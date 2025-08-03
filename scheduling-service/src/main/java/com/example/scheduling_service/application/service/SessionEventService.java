package com.example.scheduling_service.application.service;

import com.example.scheduling_service.application.port.SessionEditUseCase;
import com.example.scheduling_service.application.port.SessionEventUseCase;
import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.domain.domainServices.SessionDomainService;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SessionEventService implements SessionEventUseCase {

    private final SessionDomainService sessionDomainService;
    private final SessionCommandRepositoryPort sessionCommandPort;
    private final SessionEditUseCase sessionEditUseCase;

    @Override
    @Transactional
    public void handle(SessionEvent event) {
        log.info("EVENT LAUNCH :: {}", event.toString());
        Session session = switch (event.eventType()){
            case SESSION_ADDED -> sessionDomainService.handleScheduledEvent((SessionScheduledEvent) event);
            case SESSION_NEAR_TO_BEGIN -> sessionDomainService.handleSessionNearToBegin((SessionNearToBeginEvent) event);
            case SESSION_SETUP -> sessionDomainService.handleSessionSetup((SessionSetupBeginEvent) event);
            case SESSION_BEGIN -> sessionDomainService.handleSessionBegin((SessionBeginEvent) event);
            case SESSION_ENDED -> sessionDomainService.handleSessionEndedEvent((SessionEndEvent) event);
            case SESSION_REMOVED -> {
                Session used = sessionDomainService.handleSessionRemoved((SessionRemovedEvent) event);
                sessionCommandPort.removeScheduledSession(used.getId());
                sessionEditUseCase.changePendingCommandsStatusToFailed(used.getId());
                yield null;
            }
            case SESSION_EDITED -> sessionEditUseCase.runSessionPendingCommands(SessionIdVO.from(((SessionChangedEvent) event).sessionId()));
        };

        if(session == null) return;
        sessionCommandPort.saveSession(session);
    }

}
