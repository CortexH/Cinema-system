package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "session_queue")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_name")
    private String sessionMovieName;

    @Column(name = "session_begin_time")
    private LocalDateTime sessionBeginTime;

    @Column(name = "session_end_time")
    private LocalDateTime sessionEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_state")
    private SessionScheduleState sessionScheduleState;
}
