package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.implementation;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.out.SessionRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper.SessionMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository.SessionRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SessionRepositoryAdapter implements SessionRepositoryPort {

    private final SessionRepositoryJPA repository;

    @Override
    public void removeScheduledSession(Boolean autoFillBlank, List<SessionIdVO> ids) {

    }

    @Override
    public Session insertNewSession(Session session) {
        return null;
    }

    @Override
    public List<Session> findByState(SessionScheduleState state) {
        return repository.findBySessionScheduleState(state).stream().map(SessionMapper::toInbound).toList();
    }

    @Override
    public List<Session> findAllSessions(Integer limit) {
        return repository.findAllSessionsByLimit(limit).stream().map(SessionMapper::toInbound).toList();
    }

    @Override
    public List<Session> findAllSessions() {
        return new ArrayList<>(repository.findAll().stream().map(SessionMapper::toInbound).toList());
    }

    @Override
    public List<Session> findSessionsOfDeterminedMovie(UUID movieId) {
        return List.of();
    }
}
