package com.example.room_service.infrastructure.adapter.inbound.web.controller;

import com.example.room_service.application.dto.event.publisher.RoomCreatedEventDTO;
import com.example.room_service.application.dto.request.CreateRoomRequestDTO;
import com.example.room_service.domain.enums.SeatState;
import com.example.room_service.domain.port.out.RoomEventPublisherPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomEventPublisherPort roomEventPublisherPort;

    @Test
    @DisplayName("Deve criar uma sala com sucesso e retornar 200 com o payload.")
    @Transactional
    void shouldCreateRoomSuccessfullyAndReturnSuccess() throws Exception {
        int rows = 2;
        int seatsPerRow = 5;

        int expectedSize = rows * seatsPerRow;

        CreateRoomRequestDTO request = new CreateRoomRequestDTO(
                "Sala teste integração", rows, seatsPerRow
        );

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.seats").isArray())
                .andExpect(jsonPath("$.seats", hasSize(expectedSize)))
                .andExpect(jsonPath("$.seats[0].seat_number").value("A1"))
                .andExpect(jsonPath("$.seats[0].state").value(SeatState.FREE.name()))
                .andExpect(jsonPath("$.seats[5].seat_number").value("B1"));

        verify(roomEventPublisherPort, times(1)).publishRoomCreated(any(RoomCreatedEventDTO.class));

    }

    @Test
    @DisplayName("Deverá retornar 'conflict' quando tentar reservar um assento já ocupado")
    @Transactional
    void shouldReturnConflictWhenReserveAnReservedSeat() throws Exception {

        CreateRoomRequestDTO create = new CreateRoomRequestDTO("Sala conflito", 1, 1);

        String createJson = objectMapper.writeValueAsString(create);

        String createRoomJson = mockMvc.perform(post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String roomId = objectMapper.readTree(createRoomJson).get("id").asText();
        String seatNumber = objectMapper.readTree(createRoomJson)
                .get("seats").get(0).get("seat_number").asText();

        String reserveEndpoint = String.format("/api/v1/rooms/%s/seats/%s/reserve", roomId, seatNumber);

        mockMvc.perform(post(reserveEndpoint))
                .andExpect(status().isOk());

        verify(roomEventPublisherPort, times(1)).publishSeatReserved(any());

        mockMvc.perform(post(reserveEndpoint))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.CONFLICT.getReasonPhrase()));

        verify(roomEventPublisherPort, times(1)).publishSeatReserved(any());
    }


}
