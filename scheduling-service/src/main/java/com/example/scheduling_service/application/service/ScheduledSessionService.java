package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.domainServices.SessionDomainService;
import com.example.scheduling_service.domain.domainServices.SessionEditDomainService;
import com.example.scheduling_service.domain.exception.SessionConflictException;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.ScheduledSessionUseCase;
import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditCommandPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionService implements ScheduledSessionUseCase {

    private final SessionCommandRepositoryPort commandPort;
    private final SessionEditCommandPort sessionEditCommandPort;
    private final SessionEditQueryPort sessionEditQueryPort;

    private final SessionQueryRepositoryPort queryPort;
    private final SessionDomainService sessionDomainService;
    private final SessionEditDomainService sessionEditDomainService;

    @Transactional("transactionManager")
    @Override
    public void removeAndReplaceScheduledSession(Boolean replace, SessionIdVO sessionId) {
        Session session = queryPort.findById(sessionId)
                .orElseThrow(() -> new NoSuchElementException("Sessão com o ID " + sessionId.value() + " não encontrada."));
        session.removeSession();
        commandPort.saveSession(session);

        if(replace){
            List<SessionEditCommand> items =  sessionEditDomainService.rearrangeNextSessions(session);
            sessionEditCommandPort.saveInBatch(items);
        }
    }

    @Override
    public List<Session> findAllSessions(Integer limitDay) {
        return queryPort.findAllSessions(limitDay);
    }

    @Override
    public Session insertNewSession(Session session) {
        Session previousSession = queryPort.findPreviousSession(session).orElse(null);
        Session nextSession = queryPort.findNextSession(session).orElse(null);

        session.validateIfOverlapsWith(nextSession);
        session.validateIfOverlapsWith(previousSession);

        List<SessionEditCommand> overlapCommands = sessionEditQueryPort.findAllPendingByTimeRangeOrdered(
                session.getSessionBeginTime(), session.getSessionEndTime());

        if(!overlapCommands.isEmpty()){
            StringBuilder builder = new StringBuilder();

            builder.append("Conflito de horário: a sessão se sobrepõe a uma sessão pendente dos horários: ");

            for(SessionEditCommand command : overlapCommands){
                builder.append(command.sessionBeginTime().toString()).append(" a ")
                        .append(command.sessionEndTime().toString())
                        .append(", ");
            }
            throw new SessionConflictException(builder.toString());
        }

        session.validateNewSession();

        return commandPort.saveSession(session);
    }

    @Override
    public List<Session> findNowWorkingSessions() {
        return List.of();
    }

}
