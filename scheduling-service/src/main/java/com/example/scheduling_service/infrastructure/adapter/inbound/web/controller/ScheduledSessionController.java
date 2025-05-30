package com.example.scheduling_service.infrastructure.adapter.inbound.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/v1/scheduler-sessions")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScheduledSessionController {

    @GetMapping
    public ResponseEntity<?> findSessions(){
        return ResponseEntity.noContent().build();
    }

}
