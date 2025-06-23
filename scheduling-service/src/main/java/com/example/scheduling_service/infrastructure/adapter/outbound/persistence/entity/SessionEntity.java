package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.converter.DurationConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "session_queue")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SessionEntity {

    @Id
    private UUID id;

    @Column(name = "session_movie_id")
    private UUID sessionMovieId;

    @Column(name = "session_begin_time")
    private LocalDateTime sessionBeginTime;

    @Column(name = "session_end_time")
    private LocalDateTime sessionEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_state")
    private SessionScheduleState sessionScheduleState;

    @Column(name = "room_id")
    private UUID roomId;

    @Convert(converter = DurationConverter.class)
    private Duration setupBeginTime;

    @Convert(converter = DurationConverter.class)
    private Duration setupEndTime;

    private Duration movieDuration;

}
