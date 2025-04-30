package com.example.ticket_service.infrastructure.adapter.outbound.web;

import com.example.ticket_service.application.dto.internal.GenericErrorDTO;
import com.example.ticket_service.domain.exception.ReserveValidationFailedException;
import com.example.ticket_service.domain.port.out.RoomGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoomGatewayAdapter implements RoomGateway {

    private final WebClient webClientRoomGateway;

    @Override
    public Boolean validateSeatReserveInBatch(String roomName, List<String> seatNumber) {

        return webClientRoomGateway.post()
                .uri("/api/v1/rooms/{roomName}/seat/validate", roomName)
                .bodyValue(seatNumber)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(GenericErrorDTO.class)
                            .flatMap(error -> {
                                String message = "Erro de validação do assento";
                                if (error != null && error.message() != null && !error.message().isBlank()) {
                                    message = error.message();
                                }
                                return Mono.<Throwable>error(new ReserveValidationFailedException(message));
                            })
                                .switchIfEmpty(Mono.error(new ReserveValidationFailedException(
                                    "Erro na validação do assento. Sem resposta ou corpo."
                            )))
                )
                .bodyToMono(Boolean.class)
                .block();
    }

    @Override
    public Boolean lockSeat(String roomName, String seatNumbers) {
        return null;
    }
}
