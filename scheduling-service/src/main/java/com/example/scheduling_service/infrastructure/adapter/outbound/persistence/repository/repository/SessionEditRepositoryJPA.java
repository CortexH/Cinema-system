package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository;

import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity.SessionEditCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SessionEditRepositoryJPA extends JpaRepository<SessionEditCommandEntity,UUID> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_edit_commands s " +
                    "WHERE s.related_session = :sessionId " +
                    "ORDER BY created_at ASC"
    )
    List<SessionEditCommandEntity> findBySessionIdAndOrder(
            @Param("sessionId") UUID sessionId
    );

    @Query(nativeQuery = true,
            value = "SELECT * FROM session_edit_commands s " +
                    "WHERE s.status = 'PENDING'" +
                    "ORDER BY created_at ASC"
    )
    List<SessionEditCommandEntity> findAllPendingAndOrder();

}
