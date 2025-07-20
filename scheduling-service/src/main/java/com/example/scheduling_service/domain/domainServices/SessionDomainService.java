package com.example.scheduling_service.domain.domainServices;

import com.example.scheduling_service.domain.domainEvents.SessionEvent;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SessionDomainService {

    private final SessionQueryRepositoryPort queryPort;
    private final SessionCommandRepositoryPort commandPort;
    private final SessionEventPublisherPort eventPublisherPort;

    public SessionDomainService(
            SessionQueryRepositoryPort queryPort,
            SessionCommandRepositoryPort commandPort,
            SessionEventPublisherPort publisher
    ){
        this.queryPort = queryPort;
        this.commandPort = commandPort;
        this.eventPublisherPort = publisher;
    }

    public void removeAndReplaceNextSessions(boolean replace, SessionIdVO sessionIdToRemove){
        Session session = queryPort.findById(sessionIdToRemove)
                .orElseThrow(() -> new NoSuchElementException("Sessão com o id especificado não encontrada."));

        session.removeSession();

        List<SessionEvent> events = new ArrayList<>(session.pullDomainEvents());

        commandPort.removeScheduledSession(sessionIdToRemove);

        if(replace){
            List<Session> nextSessions = queryPort.findAllNextSessionsFrom(session);

            Duration deletedSessionDuration = session.getTotalSessionDuration()
                    .negated();

            for(Session that : nextSessions){
                that.changeSessionTime(deletedSessionDuration);
                events.addAll(that.pullDomainEvents());
            }
            commandPort.saveInBatch(nextSessions);
        }

        eventPublisherPort.publishAll(events);

    }

}
