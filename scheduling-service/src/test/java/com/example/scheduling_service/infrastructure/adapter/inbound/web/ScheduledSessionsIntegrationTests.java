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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ScheduledSessionsIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SessionRepositoryJPA sessionRepository;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    @DisplayName("Validar adicionar sessão")
    void validateInsertSession() throws Exception {

        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plusHours(3).format(dateTimeFormatter),
                2700L, 8100L
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
    @DisplayName("Validar adicionar sessão sem enviar o campo 'movie_duration'")
    void validateInsertSessionWithoutMovieDuration() throws Exception {

        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plusHours(3).format(dateTimeFormatter),
                2700L, 8100L
        );

        Session session = SessionRequestDTOMapper.toInbound(data);

        String stringSession = objectMapper.writeValueAsString(data);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/scheduler-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringSession))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movie_duration").value(session.getMovieDuration().toSeconds()));

    }

    @Test
    @DisplayName("Validar remoção dos espaços de tempo vazios entre tempo de setup e tempo de filme")
    void validateSessionSetupTimeGapFilling() throws Exception {
        int addedSeconds = 25 * 60;
        Duration movieDuration = Duration.ofMinutes(90);
        Duration setupDuration = Duration.ofMinutes(45);
        Duration totalSessionDuration = movieDuration.plus(setupDuration).plusSeconds(addedSeconds);

        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plus(totalSessionDuration).format(dateTimeFormatter),
                setupDuration.toSeconds(), movieDuration.toSeconds()
        );

        String request = objectMapper.writeValueAsString(data);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.setup_duration").value(setupDuration.toSeconds() + addedSeconds));
    }

    // FAIL SCENARIOS

    @Test
    @DisplayName("Validar adicionar sessões com conflito entre horários")
    void validateAddConflictedSession() throws Exception {

        SessionRequestDTO session = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plusHours(3).format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO sameTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plusHours(3).format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO conflictedEndTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().minusHours(2).format(dateTimeFormatter),
                LocalDateTime.now().plusHours(1).format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO conflictedStartTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().plusHours(2).format(dateTimeFormatter),
                LocalDateTime.now().plusHours(5).format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO endTimeBeforeStartTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().plusHours(6).format(dateTimeFormatter),
                LocalDateTime.now().plusHours(5).format(dateTimeFormatter),
                2700L, 8100L
        );

        String sessionRequest = objectMapper.writeValueAsString(session);
        String sameTimeRequest = objectMapper.writeValueAsString(sameTimeSession);
        String conflictedEndTimeRequest = objectMapper.writeValueAsString(conflictedEndTimeSession);
        String conflictedStartTimeRequest = objectMapper.writeValueAsString(conflictedStartTimeSession);
        String endTimeBeforeStartTimeRequest = objectMapper.writeValueAsString(endTimeBeforeStartTimeSession);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionRequest)).andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sameTimeRequest)).andExpect(MockMvcResultMatchers.status().isConflict());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(conflictedEndTimeRequest)).andExpect(MockMvcResultMatchers.status().isConflict());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(conflictedStartTimeRequest)).andExpect(MockMvcResultMatchers.status().isConflict());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(endTimeBeforeStartTimeRequest)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Validar adicionar uma sessão com tempo de setup maior que o tempo total da sessão")
    void validateSetupDurationGreaterThanSessionDuration() throws Exception {
        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plusHours(1).format(dateTimeFormatter),
                (long) (120 * 60), 0L
        );

        String request = objectMapper.writeValueAsString(data);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("Validar adicionar uma sessão com tempo de filme maior que o tempo total da sessão")
    void validateMovieDurationGreaterThanSessionDuration() throws Exception {
        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                LocalDateTime.now().plusHours(1).format(dateTimeFormatter),
                0L, (long) (120 * 60)
        );

        String request = objectMapper.writeValueAsString(data);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/scheduler-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
