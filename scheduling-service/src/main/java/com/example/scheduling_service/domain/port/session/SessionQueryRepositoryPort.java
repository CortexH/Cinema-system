package com.example.scheduling_service.domain.port.session;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionQueryRepositoryPort {

    Optional<Session> findById(SessionIdVO id);
    List<Session> findByIdList(List<SessionIdVO> ids);
    List<Session> findByState(SessionScheduleState state);
    List<Session> findAllSessions(Integer limit);
    List<Session> findAllSessions();
    List<Session> findSessionsOfDeterminedMovie(UUID movieId);

    List<Session> findAllNextSessionsFrom(Session session);

    Optional<Session> findNextSession(Session session);
    Optional<Session> findPreviousSession(Session session);


}
