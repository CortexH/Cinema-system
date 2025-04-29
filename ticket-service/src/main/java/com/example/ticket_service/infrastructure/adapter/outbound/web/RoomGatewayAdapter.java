package com.example.ticket_service.infrastructure.adapter.outbound.web;

import com.example.ticket_service.domain.port.out.RoomGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomGatewayAdapter implements RoomGateway {


    @Override
    public Boolean validateSeatReserveInBatch(String roomName, List<String> seatNumber) {
        return true;
    }



}
