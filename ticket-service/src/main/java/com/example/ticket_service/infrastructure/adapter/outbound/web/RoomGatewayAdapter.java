package com.example.ticket_service.infrastructure.adapter.outbound.web;

import com.example.ticket_service.domain.port.out.RoomGateway;

import java.util.List;

public class RoomGatewayAdapter implements RoomGateway {


    @Override
    public Boolean validateSeatReserveInBatch(String roomName, List<String> seatNumber) {
        return true;
    }



}
