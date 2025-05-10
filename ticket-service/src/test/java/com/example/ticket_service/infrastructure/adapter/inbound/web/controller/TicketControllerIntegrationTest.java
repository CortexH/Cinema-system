package com.example.ticket_service.infrastructure.adapter.inbound.web.controller;

import com.example.ticket_service.application.dto.event.TicketCreatedEventDTO;
import com.example.ticket_service.application.dto.event.TicketCreationFailedEventDTO;
import com.example.ticket_service.application.dto.event.TicketRequestedEventDTO;
import com.example.ticket_service.application.dto.event.TicketUsedEventDTO;
import com.example.ticket_service.application.dto.internal.MovieInformationDTO;
import com.example.ticket_service.application.dto.internal.RealizePaymentDTO;
import com.example.ticket_service.application.dto.request.GenerateTicketRequest;
import com.example.ticket_service.application.dto.response.TicketResponse;
import com.example.ticket_service.domain.model.Ticket;
import com.example.ticket_service.domain.port.out.MovieGateway;
import com.example.ticket_service.domain.port.out.PaymentGateway;
import com.example.ticket_service.domain.port.out.RoomGateway;
import com.example.ticket_service.domain.port.out.TicketEventPublisher;
import com.example.ticket_service.infrastructure.adapter.outbound.persistence.entity.TicketEntity;
import com.example.ticket_service.infrastructure.adapter.outbound.persistence.repository.repository.SpringTicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@Slf4j
public class TicketControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final SpringTicketRepository ticketRepository;

    @MockBean
    private TicketEventPublisher eventPublisher;

    @MockBean
    private final PaymentGateway paymentGateway;

    @MockBean
    private final RoomGateway roomGateway;

    @MockBean
    private final MovieGateway movieGateway;

    // CASOS DE SUCESSO

    @Test
    @DisplayName("Deve criar um ticket com sucesso e retornar 200 com o payload")
    void shouldSuccessfullyCreateTicket() throws Exception {

        MovieInformationDTO movie = new MovieInformationDTO(
                "Filme dos mocks", LocalDateTime.now().plusHours(1),
                true, 12, "dado mockado",
                false
        );

        when(paymentGateway.realizePayment(any(RealizePaymentDTO.class))).thenReturn(true);
        when(roomGateway.validateSeatReserveInBatch(anyString(), anyList())).thenReturn(true);
        when(movieGateway.getMovieDataFromRoom(anyString())).thenReturn(movie);

        GenerateTicketRequest ticketRequest = new GenerateTicketRequest(
                "MOCK", List.of("A1"), "MOCK ROOM"
        );

        String ticketJson = objectMapper.writeValueAsString(ticketRequest);

        mockMvc.perform(
                post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].qrCode").isNotEmpty())
                .andExpect(jsonPath("$[0].movie").value(movie.name()))
                .andExpect(jsonPath("$[0].movie_time").value(Matchers.startsWith(movie.time().toString().substring(0, 19))))
                .andExpect(jsonPath("$[0].room").value(ticketRequest.room()))
                .andExpect(jsonPath("$[0].seat").value(ticketRequest.seat_numbers().getFirst()))
                .andExpect(jsonPath("$[0].valid").value(true))
                .andExpect(jsonPath("$[0].in_use").value(false))
                .andExpect(jsonPath("$[0].accessibility").value(movie.accessibility()));

        verify(eventPublisher, times(1)).publishTicketRequested(any(TicketRequestedEventDTO.class));
        verify(eventPublisher, times(1)).publishTicketCreated(any(TicketCreatedEventDTO.class));

    }

    @Test
    @DisplayName("Deve usar um ticket com e retornar 200 com payload")
    void shouldSuccessfullyConciliateTicket() throws Exception {

        MovieInformationDTO movie = new MovieInformationDTO(
                "Filme dos mocks", LocalDateTime.now().plusHours(1),
                true, 14, "Faixa etária mockada",
                false
        );

        when(paymentGateway.realizePayment(any(RealizePaymentDTO.class))).thenReturn(true);
        when(roomGateway.validateSeatReserveInBatch(anyString(), anyList())).thenReturn(true);
        when(movieGateway.getMovieDataFromRoom(anyString())).thenReturn(movie);

        GenerateTicketRequest ticketRequest = new GenerateTicketRequest(
                "MOCK", List.of("A1"), "MOCK ROOM"
        );

        String ticketJson = objectMapper.writeValueAsString(ticketRequest);

        String createTicketJson = mockMvc.perform(post("/api/v1/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ticketJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode first = objectMapper.readTree(createTicketJson).get(0);

        String ticketId = first.get("id").asText();

        String ticketRoom = first.get("room").asText();
        String ticketSeat = first.get("seat").asText();


        when(roomGateway.lockSeat(anyString(), anyString())).thenReturn(true);

        String endpoint = String.format("/api/v1/tickets/conciliate/%s", ticketId);

        mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.seat_number").value(ticketSeat))
                .andExpect(jsonPath("$.room_number").value(ticketRoom));

        TicketEntity ticket = ticketRepository.findById(UUID.fromString(ticketId)).orElseThrow();

        verify(eventPublisher, times(1)).publishTicketUsed(any(TicketUsedEventDTO.class));

        Assert.isTrue(!ticket.getValid(), "Ticket retornado do banco ainda está válido");
        Assert.isTrue(ticket.getInUse(), "Ticket retornado do banco não está marcado como 'em uso'");

    }

    // CASOS DE ERRO

    @Test
    @DisplayName("Deve retornar 'ticket inválido' quando tentar usar um ticket já usado")
    void shouldReturnErrorWhenUseAlreadyUsedTicket() throws Exception {
        MovieInformationDTO movie = new MovieInformationDTO(
                "Filme dos mocks", LocalDateTime.now().plusHours(1),
                true, 14, "Faixa etária mockada",
                false
        );

        when(paymentGateway.realizePayment(any(RealizePaymentDTO.class))).thenReturn(true);
        when(roomGateway.validateSeatReserveInBatch(anyString(), anyList())).thenReturn(true);
        when(movieGateway.getMovieDataFromRoom(anyString())).thenReturn(movie);

        GenerateTicketRequest ticketRequest = new GenerateTicketRequest(
                "MOCK", List.of("A1"), "MOCK ROOM"
        );

        String ticketJson = objectMapper.writeValueAsString(ticketRequest);

        String createTicketJson = mockMvc.perform(post("/api/v1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode first = objectMapper.readTree(createTicketJson).get(0);

        String ticketId = first.get("id").asText();

        when(roomGateway.lockSeat(anyString(), anyString())).thenReturn(true);

        String endpoint = String.format("/api/v1/tickets/conciliate/%s", ticketId);

        mockMvc.perform(get(endpoint)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(eventPublisher, times(1)).publishTicketUsed(any(TicketUsedEventDTO.class));


        mockMvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Ticket inválido"))
                .andExpect(jsonPath("$.path").value(endpoint));

        verify(eventPublisher, times(1)).publishTicketUsed(any(TicketUsedEventDTO.class));

    }

    @Test
    @DisplayName("Deve retornar 'falha no pagamento' com payload")
    void shouldReturnErrorWhenPaymentFail() throws Exception {

        MovieInformationDTO movie = new MovieInformationDTO(
                "Filme dos mocks", LocalDateTime.now().plusHours(1),
                true, 14, "Faixa etária mockada",
                false
        );

        when(paymentGateway.realizePayment(any(RealizePaymentDTO.class))).thenReturn(false);

        when(roomGateway.validateSeatReserveInBatch(anyString(), anyList())).thenReturn(true);
        when(movieGateway.getMovieDataFromRoom(anyString())).thenReturn(movie);

        GenerateTicketRequest ticketRequest = new GenerateTicketRequest(
                "MOCK", List.of("A1"), "MOCK ROOM"
        );

        String ticketJson = objectMapper.writeValueAsString(ticketRequest);

        mockMvc.perform(post("/api/v1/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(ticketJson))
                .andExpect(status().isPaymentRequired())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Falha no pagamento"))
                .andExpect(jsonPath("$.error").value(HttpStatus.PAYMENT_REQUIRED.getReasonPhrase()));

        verify(eventPublisher, times(1)).publishTicketRequested(any(TicketRequestedEventDTO.class));
        verify(eventPublisher, times(1)).publishTicketCreationFail(any(TicketCreationFailedEventDTO.class));

    }
}
