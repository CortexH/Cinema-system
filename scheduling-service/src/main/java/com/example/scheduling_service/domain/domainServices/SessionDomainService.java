package com.example.scheduling_service.domain.domainServices;

import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.session.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SessionDomainService {

    private final SessionQueryRepositoryPort queryPort;

    public SessionDomainService(
            SessionQueryRepositoryPort queryPort
    ){
        this.queryPort = queryPort;
    }

    private Session getSession(SessionIdVO id){
        return queryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sessão com identificador " + id + " não foi encontrada."));
    }

    public Session handleScheduledEvent(SessionScheduledEvent event){
        Session usedSession = getSession(SessionIdVO.from(event.sessionId()));
        usedSession.markSessionAs(SessionScheduleState.SCHEDULED);
        return usedSession;
    }

    public Session handleSessionNearToBegin(SessionNearToBeginEvent event){
        return getSession(SessionIdVO.from(event.sessionId()));
    }

    public Session handleSessionSetup(SessionSetupBeginEvent event){
        Session usedSession = getSession(SessionIdVO.from(event.sessionId()));
        usedSession.markSessionAs(SessionScheduleState.SETUP_IN_PROGRESS);
        return usedSession;
    }

    public Session handleSessionBegin(SessionBeginEvent event){
        Session usedSession = getSession(SessionIdVO.from(event.sessionId()));
        usedSession.markSessionAs(SessionScheduleState.NOW_WORKING);
        return usedSession;
    }

    public Session handleSessionEndedEvent(SessionEndEvent event){
        Session usedSession = getSession(SessionIdVO.from(event.sessionId()));
        usedSession.markSessionAs(SessionScheduleState.FINISHED);
        return usedSession;
    }

    public Session handleSessionRemoved(SessionRemovedEvent event){
        return getSession(SessionIdVO.from(event.sessionId()));
    }

}
