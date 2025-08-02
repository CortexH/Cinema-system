package com.example.scheduling_service.domain.dtos;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.model.SessionEditCommand;

import java.util.List;

public record EditedSessionDTO(
        Session session,
        List<SessionEditCommand> commands
) {
}
