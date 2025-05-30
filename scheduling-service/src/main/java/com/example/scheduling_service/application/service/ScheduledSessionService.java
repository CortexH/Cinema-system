package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.in.ScheduledSessionUseCase;
import com.example.scheduling_service.domain.port.out.SessionRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionService implements ScheduledSessionUseCase {

    private final SessionRepositoryPort sessionRepositoryPort;

    @Override
    public void removeAndReplaceScheduledSession(Boolean replace, List<SessionIdVO> sessionId) {

    }

    @Override
    public List<Session> findAllSessions(Integer limitDay) {
        return List.of();
    }

    @Override
    public Optional<Session> insertNewSession(Session session) {
        return Optional.empty();
    }

    @Override
    public List<Session> findNowWorkingSessions() {
        return List.of();
    }

}
