package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.exception.SessionException;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.in.ScheduledSessionUseCase;
import com.example.scheduling_service.application.port.out.SessionRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionService implements ScheduledSessionUseCase {

    private final SessionRepositoryPort sessionRepositoryPort;

    @Override
    @Transactional
    public void removeAndReplaceScheduledSession(Boolean replace, SessionIdVO sessionId) {
        Session session = sessionRepositoryPort.findById(sessionId)
                        .orElseThrow(() -> new NoSuchElementException("Sessão com o id especificado não encontrada."));

        session.validateIfAbleToRemove();

        sessionRepositoryPort.removeScheduledSession(sessionId);



    }

    @Override
    public List<Session> findAllSessions(Integer limitDay) {
        return sessionRepositoryPort.findAllSessions(limitDay);
    }

    @Override
    public Session insertNewSession(Session session) {

        Session previousSession = sessionRepositoryPort.findPreviousSession(session)
                .orElse(null);

        Session nextSession = sessionRepositoryPort.findNextSession(session)
                .orElse(null);

        log.info("PREVIOUS :: {}", previousSession);
        log.info("NEXT :: {}", previousSession);

        session.validateIfCompatibleWithNextSession(nextSession);
        session.validateIfCompatibleWithPreviousSession(previousSession);

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
