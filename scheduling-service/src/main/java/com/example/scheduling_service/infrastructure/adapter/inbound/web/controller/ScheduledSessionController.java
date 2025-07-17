package com.example.scheduling_service.infrastructure.adapter.inbound.web.controller;

import com.example.scheduling_service.application.dto.request.SessionRequestDTO;
import com.example.scheduling_service.application.dto.response.SessionDisplayDTO;
import com.example.scheduling_service.application.port.in.ScheduledSessionUseCase;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import com.example.scheduling_service.infrastructure.adapter.inbound.web.mapper.SessionDisplayMapper;
import com.example.scheduling_service.infrastructure.adapter.inbound.web.mapper.SessionRequestDTOMapper;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/scheduler-sessions")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionController {

    private final ScheduledSessionUseCase sessionUseCase;

    @GetMapping
    public ResponseEntity<?> findSessions(){
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<SessionDisplayDTO> insertNewSession(
            @RequestBody SessionRequestDTO body
    ){
        Session session = sessionUseCase.insertNewSession(SessionRequestDTOMapper.toInbound(body));
        return ResponseEntity.status(HttpStatus.CREATED).body(SessionDisplayMapper.toDTO(session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSession(
            @PathParam("replace") Boolean replace,
            @PathVariable("id") String id)
    {
        sessionUseCase.removeAndReplaceScheduledSession(replace, SessionIdVO.from(id));
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping
    public ResponseEntity<?> editSession(
            @PathParam("sessionId") String sessionId,
            @RequestBody SessionRequestDTO body
    ){
        return ResponseEntity.noContent().build();
    }

}
