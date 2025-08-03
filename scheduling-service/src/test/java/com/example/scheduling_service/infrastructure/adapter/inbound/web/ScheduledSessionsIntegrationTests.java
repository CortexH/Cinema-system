package com.example.scheduling_service.infrastructure.adapter.inbound.web;

import com.example.scheduling_service.application.dto.request.SessionRequestDTO;
import com.example.scheduling_service.application.dto.response.SessionDisplayDTO;
import com.example.scheduling_service.application.service.SessionEditService;
import com.example.scheduling_service.domain.domainEvents.SessionEvent;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.port.session.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditCommandPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import com.example.scheduling_service.infrastructure.adapter.inbound.web.mapper.SessionRequestDTOMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper.SessionMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository.SessionRepositoryJPA;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ScheduledSessionsIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SessionRepositoryJPA sessionRepository;
    private final SessionEditService sessionEditService;

    @MockBean
    private final SessionEventPublisherPort sessionEventPort;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    @DisplayName("Validar adicionar sessão")
    void validateInsertSession() throws Exception {

        SessionRequestDTO data = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
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

    //@Test refatorar teste
    @DisplayName("Validar realizar remoção de sessão com 'replace' como 'true' ")
    void validateSessionStateRemoveWithReplace() throws Exception {

        LocalDateTime firstSessionBeginTime = LocalDateTime.now().plusDays(2);

        SessionRequestDTO firstSessionRequest = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                firstSessionBeginTime.format(dateTimeFormatter),
                3000L, 6000L
        );

        Session firstSession = SessionRequestDTOMapper.toInbound(firstSessionRequest);

        SessionRequestDTO secondSessionRequest = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                firstSession.getSessionEndTime().format(dateTimeFormatter),
                3000L, 6000L
        );

        Session secondSession = SessionRequestDTOMapper.toInbound(secondSessionRequest);

        LocalDateTime firstSessionEndTime = firstSession.getSessionEndTime();
        LocalDateTime secondSessionEndTime = secondSession.getSessionEndTime();

        Duration firstSessionTotalDuration = Duration.between(firstSessionBeginTime, firstSessionEndTime).negated();

        String firstRequest = objectMapper.writeValueAsString(firstSessionRequest);
        String secondRequest = objectMapper.writeValueAsString(secondSessionRequest);

        String firstResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/scheduler-sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secondResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/scheduler-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SessionDisplayDTO firstData = objectMapper.readValue(firstResponse, SessionDisplayDTO.class);
        UUID firstId = firstData.session_id();

        SessionDisplayDTO secondData = objectMapper.readValue(secondResponse, SessionDisplayDTO.class);
        UUID secondId = secondData.session_id();

        mockMvc.perform(MockMvcRequestBuilders.delete(
                "/api/v1/scheduler-sessions/{id}?replace={replace}",
                firstId, "true"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());

        sessionEditService.runSessionPendingCommands(SessionIdVO.from(firstId));
        sessionEditService.runSessionPendingCommands(SessionIdVO.from(secondId));

        List<Session> sessions = sessionRepository.findAll()
                .stream().map(SessionMapper::toInbound).toList();

        Session notRemovedSession = sessions.stream().filter(i -> i.getId().value().equals(secondId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sessão com o id " + secondId + " não foi encontrada"));

        boolean isBeginCorrect = notRemovedSession.getSessionBeginTime().truncatedTo(ChronoUnit.SECONDS)
                        .isEqual(firstSessionEndTime.truncatedTo(ChronoUnit.SECONDS).plus(firstSessionTotalDuration));

        Assert.isTrue(
                isBeginCorrect,
                "O tempo de inicio da sessão não confere com o tempo que deveria ser.\n" +
                        "Tempo esperado: " + firstSessionEndTime.truncatedTo(ChronoUnit.SECONDS).plus(firstSessionTotalDuration) +
                        "\nTempo atual: " + notRemovedSession.getSessionBeginTime()
        );

        boolean isEndCorrect = notRemovedSession.getSessionEndTime().truncatedTo(ChronoUnit.SECONDS)
                        .isEqual(secondSessionEndTime.truncatedTo(ChronoUnit.SECONDS).plus(firstSessionTotalDuration));

        Assert.isTrue(
                isEndCorrect,
                "O tempo de inicio da sessão não confere com o tempo que deveria ser.\n" +
                        "Tempo esperado: " + firstSessionBeginTime +
                        "\nTempo atual: " + notRemovedSession.getSessionBeginTime()
        );

        ArgumentCaptor<List<SessionEvent>> eventListCaptor = ArgumentCaptor.forClass(List.class);

        Mockito.verify(sessionEventPort, Mockito.times(1)).publishAll(eventListCaptor.capture());

        List<SessionEvent> events = eventListCaptor.getValue();

        Assertions.assertThat(events.size()).isEqualTo(4);

    }

    @Test
    @DisplayName("Validar realizar remoção de sessão com 'replace' como 'false' ")
    void validateSessionStateRemoveWithoutReplace() throws Exception {
        LocalDateTime firstSessionBeginTime = LocalDateTime.now().plusDays(2);

        SessionRequestDTO firstSessionRequest = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                firstSessionBeginTime.format(dateTimeFormatter),
                3000L, 6000L
        );

        Session firstSession = SessionRequestDTOMapper.toInbound(firstSessionRequest);

        SessionRequestDTO secondSessionRequest = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                firstSession.getSessionEndTime().format(dateTimeFormatter),
                3000L, 6000L
        );

        Session secondSession = SessionRequestDTOMapper.toInbound(secondSessionRequest);

        LocalDateTime firstSessionEndTime = firstSession.getSessionEndTime();
        LocalDateTime secondSessionEndTime = secondSession.getSessionEndTime();

        String firstRequest = objectMapper.writeValueAsString(firstSessionRequest);
        String secondRequest = objectMapper.writeValueAsString(secondSessionRequest);

        String firstResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/scheduler-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secondResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/scheduler-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();



        SessionDisplayDTO firstData = objectMapper.readValue(firstResponse, SessionDisplayDTO.class);
        UUID firstId = firstData.session_id();

        SessionDisplayDTO secondData = objectMapper.readValue(secondResponse, SessionDisplayDTO.class);
        UUID secondId = secondData.session_id();

        mockMvc.perform(MockMvcRequestBuilders.delete(
                        "/api/v1/scheduler-sessions/{id}?replace={replace}",
                        firstId, "false"))
                .andExpect(MockMvcResultMatchers.status().isAccepted());

        List<Session> sessions = sessionRepository.findAll()
                .stream().map(SessionMapper::toInbound).toList();

        Session notRemovedSession = sessions.stream().filter(i -> i.getId().value().equals(secondId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sessão com o id " + secondId + " não foi encontrada"));

        boolean isBeginCorrect = notRemovedSession.getSessionBeginTime().truncatedTo(ChronoUnit.SECONDS)
                .isEqual(firstSessionEndTime.truncatedTo(ChronoUnit.SECONDS));

        Assert.isTrue(
                isBeginCorrect,
                "O tempo de inicio da sessão não confere com o tempo que deveria ser.\n" +
                        "Tempo esperado: " + firstSessionEndTime.truncatedTo(ChronoUnit.SECONDS) +
                        "\nTempo atual: " + notRemovedSession.getSessionBeginTime()
        );

        boolean isEndCorrect = notRemovedSession.getSessionEndTime().truncatedTo(ChronoUnit.SECONDS)
                .isEqual(secondSessionEndTime.truncatedTo(ChronoUnit.SECONDS));

        Assert.isTrue(
                isEndCorrect,
                "O tempo de inicio da sessão não confere com o tempo que deveria ser.\n" +
                        "Tempo esperado: " + secondSessionEndTime.truncatedTo(ChronoUnit.SECONDS) +
                        "\nTempo atual: " + notRemovedSession.getSessionEndTime().truncatedTo(ChronoUnit.SECONDS)
        );

    }

    // FAIL SCENARIOS

    @Test
    @DisplayName("Validar adicionar sessões com conflito entre horários")
    void validateAddConflictedSession() throws Exception {

        SessionRequestDTO session = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO sameTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO conflictedEndTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().minusHours(2).format(dateTimeFormatter),
                2700L, 8100L
        );

        SessionRequestDTO conflictedStartTimeSession = new SessionRequestDTO(
                UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now().plusHours(2).format(dateTimeFormatter),
                2700L, 8100L
        );


        String sessionRequest = objectMapper.writeValueAsString(session);
        String sameTimeRequest = objectMapper.writeValueAsString(sameTimeSession);
        String conflictedEndTimeRequest = objectMapper.writeValueAsString(conflictedEndTimeSession);
        String conflictedStartTimeRequest = objectMapper.writeValueAsString(conflictedStartTimeSession);

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

    }

}
