package com.example.scheduling_service.domain.port.session;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.List;

public interface SessionCommandRepositoryPort {

    void removeScheduledSession(SessionIdVO id);
    Session saveSession(Session session);
    List<Session> saveInBatch(List<Session> sessions);

}
