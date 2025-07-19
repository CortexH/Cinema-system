package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.domainEvents.SessionEvent;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.SessionSchedulerUseCase;
import com.example.scheduling_service.domain.port.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.SessionQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduleService implements SessionSchedulerUseCase {

    private final SessionCommandRepositoryPort commandPort;
    private final SessionQueryRepositoryPort queryPort;
    private final SessionEventPublisherPort sessionEventPublisher;

    @Override
    public void scheduleNextSessions(Integer days) {

    }

    @Override
    public void rescheduleActualSessions() {

    }

    @Override
    @Transactional("transactionManager")
    public void runScheduledCheckout() {
        List<Session> allSessions = queryPort.findAllSessions();

        List<SessionEvent> eventsToPublish = new ArrayList<>();
        List<Session> changedSessions = new ArrayList<>();

        for(Session s : allSessions){
            if(s.syncStateWithLocalTime()) changedSessions.add(s);
            eventsToPublish.addAll(s.pullDomainEvents());
        }

        if(eventsToPublish.isEmpty() && changedSessions.isEmpty()) return;
        if(!eventsToPublish.isEmpty()) sessionEventPublisher.publishAll(eventsToPublish);
        if(!changedSessions.isEmpty()) commandPort.saveInBatch(changedSessions);
    }
}
