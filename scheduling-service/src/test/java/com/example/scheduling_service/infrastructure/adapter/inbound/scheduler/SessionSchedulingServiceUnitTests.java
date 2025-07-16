package com.example.scheduling_service.infrastructure.adapter.inbound.scheduler;

import com.example.scheduling_service.application.service.ScheduleService;
import com.example.scheduling_service.domain.domainEvents.SessionEvent;
import com.example.scheduling_service.domain.enums.SessionEventType;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.application.port.out.SessionEventPublisherPort;
import com.example.scheduling_service.application.port.out.SessionRepositoryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SessionSchedulingServiceUnitTests {

    @Test
    @DisplayName("Validar ter uma sessão 'Scheduled' já terminada")
    @Transactional
    public void validarTerUmaSessaoScheduledJaTerminada(){
        SessionRepositoryPort sessionPort = mock(SessionRepositoryPort.class);
        SessionEventPublisherPort sessionEventPublisher = mock(SessionEventPublisherPort.class);

        when(sessionPort.findAllSessions()).thenReturn(new ArrayList<>(List.of(new Session(
                SessionIdVO.generate(),
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().plusHours(-3), LocalDateTime.now().plusHours(-2),
                SessionScheduleState.SCHEDULED,
                Duration.ofMinutes(10), Duration.ofMinutes(45)))));

        ScheduleService service = new ScheduleService(sessionPort, sessionEventPublisher);

        service.runScheduledCheckout();

        ArgumentCaptor<List<SessionEvent>> captor = ArgumentCaptor.forClass(List.class);

        verify(sessionEventPublisher).publishAll(captor.capture());
        List<SessionEvent> events = captor.getValue();

        assertEquals(4, events.size());
        assertEquals(SessionEventType.SESSION_ADDED, events.get(0).eventType());
        assertEquals(SessionEventType.SESSION_SETUP, events.get(1).eventType());
        assertEquals(SessionEventType.SESSION_BEGIN, events.get(2).eventType());
        assertEquals(SessionEventType.SESSION_ENDED, events.get(3).eventType());
    }

    @Test
    @DisplayName("Validar scheduler ao adicionar nova sessão")
    @Transactional
    public void validarAdicionarSessao(){
        SessionRepositoryPort sessionPort = mock(SessionRepositoryPort.class);
        SessionEventPublisherPort sessionEventPublisher = mock(SessionEventPublisherPort.class);

        when(sessionPort.findAllSessions()).thenReturn(new ArrayList<>(List.of(new Session(
                SessionIdVO.generate(),
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(11),
                SessionScheduleState.SCHEDULED,
                Duration.ofMinutes(10), Duration.ofMinutes(45)))));

        ScheduleService service = new ScheduleService(sessionPort, sessionEventPublisher);

        service.runScheduledCheckout();

        ArgumentCaptor<List<SessionEvent>> captor = ArgumentCaptor.forClass(List.class);

        verify(sessionEventPublisher).publishAll(captor.capture());
        List<SessionEvent> events = captor.getValue();

        assertEquals(1, events.size());
        assertEquals(SessionEventType.SESSION_ADDED, events.getFirst().eventType());

    }

    @Test
    @DisplayName("Validar scheduler ao adicionar uma grande quantidade de sessões")
    public void validarSchedulerAoAdicionarMuitasSessoes(){
        SessionRepositoryPort sessionPort = mock(SessionRepositoryPort.class);
        SessionEventPublisherPort eventPublisherPort = mock(SessionEventPublisherPort.class);

        int quantidadeSessoes = (int) Math.round(Math.random() * 100);

        List<Session> usedSessions = new ArrayList<>();

        for (int i = 0; i < quantidadeSessoes; i++) {
            usedSessions.add(new Session(
                    SessionIdVO.generate(),
                    UUID.randomUUID(), UUID.randomUUID(),
                    LocalDateTime.now().plusHours(10), LocalDateTime.now().plusHours(11),
                    SessionScheduleState.SCHEDULED,
                    Duration.ofMinutes(10), Duration.ofMinutes(45)));
        }

        when(sessionPort.findAllSessions()).thenReturn(usedSessions);

        ScheduleService service = new ScheduleService(sessionPort, eventPublisherPort);

        service.runScheduledCheckout();

        ArgumentCaptor<List<SessionEvent>> captor = ArgumentCaptor.forClass((Class) List.class);
        verify(eventPublisherPort).publishAll(captor.capture());
        List<SessionEvent> events = captor.getValue();

        assertEquals(quantidadeSessoes, events.size());

        for (SessionEvent event : events){
            assertEquals(SessionEventType.SESSION_ADDED, event.eventType());
        }
    }

}
