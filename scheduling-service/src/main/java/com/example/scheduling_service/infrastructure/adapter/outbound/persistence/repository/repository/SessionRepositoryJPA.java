package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SessionRepositoryJPA extends JpaRepository<SessionEntity, UUID> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_queue s " +
                    "WHERE s.schedule_state = 'NOW_WORKING'"
    )
    List<SessionEntity> findNowWorkingSessions();

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_queue s " +
                    "WHERE s.scheduled_state = 'SCHEDULED'"
    )
    List<SessionEntity> findScheduledSessions();

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_queue s " +
                    "WHERE s.scheduled_state = :state " +
                    "ORDER BY s.session_begin_time ASC"
    )
    List<SessionEntity> findBySessionScheduleState(@Param("state") SessionScheduleState state);

    @Query(nativeQuery = true,
            value = "SELECT top :limit * FROM session_queue"
    )
    List<SessionEntity> findAllSessionsByLimit(@Param("limit") Integer limit);

}
