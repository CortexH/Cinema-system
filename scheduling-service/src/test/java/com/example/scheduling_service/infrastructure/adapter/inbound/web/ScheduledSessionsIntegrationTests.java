package com.example.scheduling_service.infrastructure.adapter.inbound.web;

import com.example.scheduling_service.application.dto.request.SessionRequestDTO;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.infrastructure.adapter.inbound.web.mapper.SessionRequestDTOMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper.SessionMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository.SessionRepositoryJPA;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionsIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SessionRepositoryJPA sessionRepository;

    @Test
    @DisplayName("Deverá inserir uma sessão com sucesso")
    @Transactional
    void testInsertSession() throws Exception {

        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                "2025-07-16T14:00:00",
                "2025-07-16T17:00:00",
                2700L, 540000L
        );

        Session requestSession = SessionRequestDTOMapper.toInbound(data);

        String request = objectMapper.writeValueAsString(data);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Session databaseSession = sessionRepository.findAll()
                .stream().map(SessionMapper::toInbound).toList().getFirst();

        Assert.isTrue(databaseSession.getSessionBeginTime().equals(requestSession.getSessionBeginTime()), "o campo 'beginTime' de ambas as sessões não condizem");
        Assert.isTrue(databaseSession.getSessionEndTime().equals(requestSession.getSessionEndTime()), "o campo 'endTime' de ambas as sessões não condizem");
        Assert.isTrue(databaseSession.getMovieId().equals(requestSession.getMovieId()), "o campo 'movieId' de ambas as sessões não condizem");
        Assert.isTrue(databaseSession.getRoomId().equals(requestSession.getRoomId()), "o campo 'roomId' de ambas as sessões não condizem");
        Assert.isTrue(databaseSession.getSetupTime().equals(requestSession.getSetupTime()), "o campo 'setupTime' de ambas as sessões não condizem");
    }

    @Test
    @DisplayName("Validar remover sessão já agendada com replace desativado")
    void testRemoveScheduledSession(){
        SessionRequestDTO sessionRequestFirst = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                "2025-07-16T14:00:00",
                "2025-07-16T17:00:00",
                2700L, 540000L
        );

        SessionRequestDTO sessionRequestLast = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                "2025-07-16T17:00:00",
                "2025-07-16T20:00:00",
                2700L, 540000L
        );

    }

    void testAddConflictedSession(){
        SessionRequestDTO sessionRequestFirst = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                "2025-07-16T14:00:00",
                "2025-07-16T17:00:00",
                2700L, 540000L
        );

        SessionRequestDTO sessionRequestLast = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                "2025-07-16T15:00:00",
                "2025-07-16T18:00:00",
                2700L, 540000L
        );



    }

}
