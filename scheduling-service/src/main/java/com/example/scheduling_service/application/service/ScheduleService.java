package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.domainEvents.SessionEvent;
import com.example.scheduling_service.domain.dtos.SessionEditDTO;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.SessionSchedulerUseCase;
import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.session.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditCommandPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduleService implements SessionSchedulerUseCase {

    private final SessionCommandRepositoryPort sessionCommandPort;
    private final SessionQueryRepositoryPort sessionQueryPort;
    private final SessionEventPublisherPort sessionEventPublisher;

    private final SessionEditQueryPort sessionEditQueryPort;
    private final SessionEditCommandPort sessionEditCommandPort;

    @Override
    public void scheduleNextSessions(Integer days) {

    }

    @Override
    public void rescheduleActualSessions() {

    }

    // Estruturar melhor ambos esses métodos.
    @Override
    //@Transactional("transactionManager")
    public void runScheduledCheckout() {
        List<Session> allSessions = findSessions();

        List<SessionEvent> eventsToPublish = new ArrayList<>();

        for(Session s : allSessions){
            s.syncStateWithLocalTime();
            eventsToPublish.addAll(s.pullDomainEvents());
        }

        if(!eventsToPublish.isEmpty()) sessionEventPublisher.publishAll(eventsToPublish);
    }

    private List<Session> findSessions(){
        List<SessionEditCommand> commands = sessionEditQueryPort.findAllPendingOrdered();
        List<Session> allSessions = sessionQueryPort.findAllSessions();

        for(SessionEditCommand command : commands){
            SessionEditDTO dto = new SessionEditDTO(
                    command.movieId(), command.roomId(),
                    command.sessionBeginTime(), command.sessionEndTime(),
                    command.setupTime(), command.movieDuration(),
                    command.sessionScheduleState());

            Session session = allSessions.stream().filter(i -> i.getId().value().equals(command.relatedSessionId()))
                    .findFirst().orElse(null);

            if(session == null) continue;

            session.editSession(dto);
        }

        return allSessions;

    }

}
