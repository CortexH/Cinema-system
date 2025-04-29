package com.example.ticket_service.infrastructure.adapter.outbound.web;

import com.example.ticket_service.domain.port.out.RoomGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomGatewayAdapter implements RoomGateway {

    private final WebClient webClient;

    @Override
    public Boolean validateSeatReserveInBatch(String roomName, List<String> seatNumber) {

        webClient.get();

        return true;
    }

    @Override
    public Boolean lockSeat(String roomName, String seatNumbers) {
        return null;
    }


}
