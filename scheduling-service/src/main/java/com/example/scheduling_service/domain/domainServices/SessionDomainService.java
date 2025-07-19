package com.example.scheduling_service.domain.domainServices;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.NoSuchElementException;

public class SessionDomainService {

    private final SessionQueryRepositoryPort queryPort;
    private final SessionCommandRepositoryPort commandPort;

    public SessionDomainService(SessionQueryRepositoryPort queryPort, SessionCommandRepositoryPort commandPort){
        this.queryPort = queryPort;
        this.commandPort = commandPort;
    }

    public void removeAndReplaceNextSessions(boolean replace, SessionIdVO sessionIdToRemove){
        Session session = queryPort.findById(sessionIdToRemove)
                .orElseThrow(() -> new NoSuchElementException("Sessão com o id especificado não encontrada."));

        session.validateIfAbleToRemove();

        commandPort.removeScheduledSession(sessionIdToRemove);

        if(!replace) return;

    }

}
