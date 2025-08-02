package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.implementation;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper.SessionMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository.SessionRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SessionRepositoryAdapter implements SessionQueryRepositoryPort, SessionCommandRepositoryPort {

    private final SessionRepositoryJPA repository;

    @Override
    public void removeScheduledSession(SessionIdVO id) {
        repository.deleteById(id.value());
    }

    @Override
    public Session saveSession(Session session) {
        return SessionMapper.toInbound(repository.save(SessionMapper.toOutbound(session)));
    }

    @Override
    public List<Session> saveInBatch(List<Session> sessions) {
        return new ArrayList<>(repository.saveAll(
                sessions.stream().map(SessionMapper::toOutbound)
                        .toList()
        ).stream().map(SessionMapper::toInbound).toList());
    }

    @Override
    public Optional<Session> findById(SessionIdVO id) {
        return repository.findById(id.value())
                .map(SessionMapper::toInbound);
    }

    @Override
    public List<Session> findByIdList(List<SessionIdVO> ids) {
        return new ArrayList<>(repository.findAllById(ids.stream().map(SessionIdVO::value).toList())
                .stream().map(SessionMapper::toInbound).toList());
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

    @Override
    public List<Session> findAllNextSessionsFrom(Session session) {
        return new ArrayList<>(repository.findAllNextSessionsFrom(session.getSessionBeginTime(), session.getId().value())
                .stream().map(SessionMapper::toInbound).toList());
    }

    @Override
    public Optional<Session> findNextSession(Session session) {
        return repository.findNextSession(session.getSessionBeginTime(), session.getId().value())
                .map(SessionMapper::toInbound);
    }

    @Override
    public Optional<Session> findPreviousSession(Session session) {
        return repository.findPreviousSession(session.getSessionBeginTime(), session.getId().value())
                .map(SessionMapper::toInbound);
    }
}
