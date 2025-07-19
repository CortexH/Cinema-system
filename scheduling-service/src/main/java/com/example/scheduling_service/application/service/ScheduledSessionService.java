package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.domainServices.SessionDomainService;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.ScheduledSessionUseCase;
import com.example.scheduling_service.domain.port.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionService implements ScheduledSessionUseCase {

    private final SessionCommandRepositoryPort commandPort;
    private final SessionQueryRepositoryPort queryPort;
    private final SessionDomainService sessionDomainService;

    @Override
    @Transactional
    public void removeAndReplaceScheduledSession(Boolean replace, SessionIdVO sessionId) {
        sessionDomainService.removeAndReplaceNextSessions(replace, sessionId);
    }

    @Override
    public List<Session> findAllSessions(Integer limitDay) {
        return queryPort.findAllSessions(limitDay);
    }

    @Override
    public Session insertNewSession(Session session) {

        Session previousSession = queryPort.findPreviousSession(session).orElse(null);

        Session nextSession = queryPort.findNextSession(session).orElse(null);

        session.validateIfOverlapsWith(nextSession);
        session.validateIfOverlapsWith(previousSession);

        session.validateNewSession();

        return commandPort.insertNewSession(session);
    }

    @Override
    public Session removeSession(SessionIdVO sessionIdVO) {
        return null;
    }

    @Override
    public List<Session> findNowWorkingSessions() {
        return List.of();
    }

}
