package com.example.scheduling_service.application.port.out;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.List;
import java.util.UUID;

public interface SessionRepositoryPort {

    void removeScheduledSession(Boolean autoFillBlank, List<SessionIdVO> ids);
    Session insertNewSession(Session session);
    List<Session> saveInBatch(List<Session> sessions);

    List<Session> findByState(SessionScheduleState state);
    List<Session> findAllSessions(Integer limit);
    List<Session> findAllSessions();
    List<Session> findSessionsOfDeterminedMovie(UUID movieId);



}
