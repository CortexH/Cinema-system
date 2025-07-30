package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_queue s " +
                    "WHERE s.session_begin_time >= :first " +
                    "AND s.session_begin_time <= :next " +
                    "ORDER BY s.session_begin_time ASC"
    )
    List<SessionEntity> findSessionsByBeginDateTimeRange(
            @Param("first") LocalDateTime first,
            @Param("next") LocalDateTime next
    );

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_queue s " +
                    "WHERE s.session_end_time >= :first " +
                    "AND s.session_end_time <= :next " +
                    "ORDER BY s.session_end_time ASC"
    )
    List<SessionEntity> findSessionsByEndDateTimeRange(
            @Param("first") LocalDateTime first,
            @Param("next") LocalDateTime next
    );

    @Query(nativeQuery = true,
            value = """
        SELECT * FROM session_queue s
        WHERE s.session_begin_time >= :beginTime
        AND s.id != :excluded
        ORDER BY s.session_begin_time ASC
        LIMIT 1
        """
    )
    Optional<SessionEntity> findNextSession(
            @Param("beginTime") LocalDateTime time,
            @Param("excluded") UUID excluded
    );

    @Query(nativeQuery = true,
            value = """
        SELECT * FROM session_queue s
        WHERE s.session_begin_time <= :beginTime
        AND s.id != :excluded
        ORDER BY s.session_begin_time DESC
        LIMIT 1
    """

    )
    Optional<SessionEntity> findPreviousSession(
            @Param("beginTime") LocalDateTime time,
            @Param("excluded") UUID excluded
    );

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_queue s " +
                    "WHERE s.session_begin_time >= :beginTime " +
                    "AND s.id != :excluded"
    )
    List<SessionEntity> findAllNextSessionsFrom(
            @Param("beginTime") LocalDateTime time,
            @Param("excluded") UUID excluded
    );

}
