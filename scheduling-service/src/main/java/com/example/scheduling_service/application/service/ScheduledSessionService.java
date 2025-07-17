package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.exception.SessionException;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.in.ScheduledSessionUseCase;
import com.example.scheduling_service.application.port.out.SessionRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionService implements ScheduledSessionUseCase {

    private final SessionRepositoryPort sessionRepositoryPort;

    @Override
    public void removeAndReplaceScheduledSession(Boolean replace, SessionIdVO sessionId) {
        sessionRepositoryPort.removeScheduledSession(sessionId);
    }

    @Override
    public List<Session> findAllSessions(Integer limitDay) {
        return sessionRepositoryPort.findAllSessions(limitDay);
    }

    @Override
    public Session insertNewSession(Session session) {
        return sessionRepositoryPort.insertNewSession(session);
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
