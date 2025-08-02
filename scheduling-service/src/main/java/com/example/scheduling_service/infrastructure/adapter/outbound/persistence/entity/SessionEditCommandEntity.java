package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity;

import com.example.scheduling_service.domain.enums.CommandStatus;
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
@Table(name = "session_edit_commands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionEditCommandEntity {

    @Id
    private UUID id;

    @Column(name = "related_session", nullable = false, updatable = false)
    private UUID relatedSession;

    @Column(nullable = false, name = "new_movie_id")
    private UUID newMovieId;

    @Column(nullable = false, name = "new_room_id")
    private UUID newRoomId;

    @Column(nullable = false, name = "new_begin_time")
    private LocalDateTime newBeginTime;

    @Column(nullable = false, name = "new_end_time")
    private LocalDateTime newEndTime;

    @Column(nullable = false, name = "new_setup_time")
    @Convert(converter = DurationConverter.class)
    private Duration setupTime;

    @Column(nullable = false, name = "new_movie_duration")
    @Convert(converter = DurationConverter.class)
    private Duration movieDuration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandStatus status;

    @Enumerated(EnumType.STRING)
    private SessionScheduleState sessionState;

    @PrePersist
    protected void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
