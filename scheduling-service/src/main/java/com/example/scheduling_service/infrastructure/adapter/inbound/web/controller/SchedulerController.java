package com.example.scheduling_service.infrastructure.adapter.inbound.web.controller;

import com.example.scheduling_service.domain.port.in.SessionSchedulerUseCase;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/scheduler-tasks")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SchedulerController {

    private final SessionSchedulerUseCase schedulerUseCase;

    @PostMapping("/schedule")
    public ResponseEntity<?> forceSessionReschedule(){
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> forceRunScheduledCheckout(){
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> forceScheduleNextSessions(@PathParam("quantity") Integer quantity){
        return ResponseEntity.noContent().build();
    }

}
