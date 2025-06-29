package com.example.scheduling_service.application.dto.event;

import com.example.scheduling_service.application.dto.internal.SessionDTO;
import com.example.scheduling_service.domain.enums.SessionEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class SessionBeginEventDTO extends SessionEventAbstract {

    private Instant timestamp;
    private SessionDTO session;
    private SessionEventType type;

    public SessionBeginEventDTO(Instant timestamp, SessionDTO session, SessionEventType type) {
        this.timestamp = timestamp;
        this.session = session;
        this.type = type;
    }
}
