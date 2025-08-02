package com.example.scheduling_service.application.service;

import com.example.scheduling_service.application.port.SessionEditUseCase;
import com.example.scheduling_service.domain.domainServices.SessionEditDomainService;
import com.example.scheduling_service.domain.dtos.EditedSessionDTO;
import com.example.scheduling_service.domain.dtos.SessionEditDTO;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditCommandPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SessionEditService implements SessionEditUseCase {

    private final SessionQueryRepositoryPort sessionQueryPort;
    private final SessionEditQueryPort sessionEditQueryPort;
    private final SessionEditCommandPort sessionEditCommandPort;
    private final SessionEditDomainService sessionEditDomainService;

    @Override
    public Session runSessionPendingCommands(SessionIdVO sessionIdVO) {
        Session session = sessionQueryPort.findById(sessionIdVO)
                .orElse(null);

        List<SessionEditCommand> commands = sessionEditQueryPort.findBySessionId(sessionIdVO);

        EditedSessionDTO editDTO = sessionEditDomainService.editSession(session, commands);

        sessionEditCommandPort.saveInBatch(editDTO.commands());

        return editDTO.session();
    }

}
