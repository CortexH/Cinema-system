package com.example.scheduling_service.domain.port.out;

import com.example.scheduling_service.domain.model.Session;

import java.util.List;

public interface SessionRepositoryPort {

    void removeScheduledSession();
    Session insertNewSession(Session session);

    List<Session> findAllSessions(Integer limit);
    List<Session> findSessionsOfDeterminedMovie();
    List<Session> findNowWorkingSessions();
    List<Session> findScheduledSessions();



}
