package com.example.ticket_service.domain.port.out;

import java.util.List;

public interface RoomGateway {

    Boolean validateSeatReserveInBatch(String roomName, List<String> seatNumber);

    Boolean lockSeat(String roomName, String seatNumbers);

}
