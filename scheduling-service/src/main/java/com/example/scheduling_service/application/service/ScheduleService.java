package com.example.scheduling_service.application.service;

import com.example.scheduling_service.application.dto.event.SessionBeginEventDTO;
import com.example.scheduling_service.application.dto.event.SessionEndedEventDTO;
import com.example.scheduling_service.application.dto.internal.SessionDTO;
import com.example.scheduling_service.domain.enums.SessionEventType;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.in.SessionSchedulerUseCase;
import com.example.scheduling_service.domain.port.out.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.out.SessionRepositoryPort;
import com.example.scheduling_service.infrastructure.adapter.outbound.web.mapper.SessionDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduleService implements SessionSchedulerUseCase {

    private final SessionRepositoryPort sessionPort;
    private final SessionEventPublisherPort sessionEventPublisher;

    @Override
    public void scheduleNextSessions(Integer days) {

    }

    @Override
    public void rescheduleActualSessions() {

    }

    @Override
    public void runScheduledCheckout() {
        List<Session> overallSession = sessionPort.findAllSessions();

        Map<SessionScheduleState, List<Session>> changedSessions = new HashMap<>();

        for(Session s : overallSession){
            boolean sessionChanged = s.changeSessionState();
            if(!sessionChanged) continue;

            changedSessions
                    .computeIfAbsent(s.getSessionScheduleState(), k -> new ArrayList<>())
                    .add(s);
        }

        if(changedSessions.isEmpty()) return;

        Map<SessionScheduleState, List<SessionDTO>> sessionsDTO = new HashMap<>();

        for(SessionScheduleState key : changedSessions.keySet()){
            sessionsDTO.put(key,
                    new ArrayList<>(changedSessions.get(key).stream().map(SessionDTOMapper::toDto).toList())
            );
        }

        if(changedSessions.containsKey(SessionScheduleState.NOW_WORKING)){
            List<SessionDTO> sessions = sessionsDTO.get(SessionScheduleState.NOW_WORKING);
            SessionBeginEventDTO event = new SessionBeginEventDTO(
                    Instant.now(),
                    sessions
            );
            sessionEventPublisher.publishSessionsStarted(event);
        }

        if(changedSessions.containsKey(SessionScheduleState.FINISHED)){
            List<SessionDTO> sessions = sessionsDTO.get(SessionScheduleState.NOW_WORKING);
            SessionEndedEventDTO event = new SessionEndedEventDTO(
                    Instant.now(),
                    sessions
            );
            sessionEventPublisher.publishSessionsEnded(event);
        }

    }

}
