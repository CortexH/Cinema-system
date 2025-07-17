package com.example.scheduling_service.application.port.out;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepositoryPort {

    void removeScheduledSession(SessionIdVO id);
    Session insertNewSession(Session session);
    List<Session> saveInBatch(List<Session> sessions);

    Optional<Session> findById(SessionIdVO id);
    List<Session> findByState(SessionScheduleState state);
    List<Session> findAllSessions(Integer limit);
    List<Session> findAllSessions();
    List<Session> findSessionsOfDeterminedMovie(UUID movieId);
    List<Session> findSessionsByBeginTimeRange(LocalDateTime first, LocalDateTime last);
    List<Session> findSessionsByEndTimeRange(LocalDateTime first, LocalDateTime last);

    Optional<Session> findNextSession(Session session);
    Optional<Session> findPreviousSession(Session session);



}
