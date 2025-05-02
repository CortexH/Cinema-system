package com.example.room_service.domain.port.out;

import com.example.room_service.application.dto.event.RoomCreatedEvent;
import com.example.room_service.application.dto.event.SeatReleasedEvent;
import com.example.room_service.application.dto.event.SeatReservedEvent;

public interface RoomEventPublisherPort {

    void publishRoomCreated(RoomCreatedEvent event);
    void publishSeatReserved(SeatReservedEvent event);
    void publishSeatReleased(SeatReleasedEvent event);

}
