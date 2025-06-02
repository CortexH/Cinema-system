package com.example.scheduling_service.domain.port.out;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.List;

public interface SessionRepositoryPort {

    void removeScheduledSession(Boolean autoFillBlank, List<SessionIdVO> ids);
    Session insertNewSession(Session session);

    List<Session> findAllSessions(Integer limit);
    List<Session> findSessionsOfDeterminedMovie();
    List<Session> findNowWorkingSessions();
    List<Session> findScheduledSessions();



}
