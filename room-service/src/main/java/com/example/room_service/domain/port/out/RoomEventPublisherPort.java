package com.example.room_service.domain.port.out;

import com.example.room_service.application.dto.event.RoomCreatedEventDTO;
import com.example.room_service.application.dto.event.SeatReleasedEventDTO;
import com.example.room_service.application.dto.event.SeatReservedEventDTO;

public interface RoomEventPublisherPort {

    void publishRoomCreated(RoomCreatedEventDTO event);
    void publishSeatReserved(SeatReservedEventDTO event);
    void publishSeatReleased(SeatReleasedEventDTO event);

}
