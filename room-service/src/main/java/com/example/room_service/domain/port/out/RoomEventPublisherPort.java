package com.example.room_service.domain.port.out;

import com.example.room_service.application.dto.event.RoomCreatedEvent;
import com.example.room_service.application.dto.event.SeatReleasedEvent;
import com.example.room_service.application.dto.event.SeatReservedEvent;
import com.example.room_service.domain.model.Room;
import com.example.room_service.domain.model.Seat;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

public interface RoomEventPublisherPort {

    void publishRoomCreated(RoomCreatedEvent event);
    void publishSeatReserved(SeatReservedEvent event);
    void publishSeatReleased(SeatReleasedEvent event);

}
