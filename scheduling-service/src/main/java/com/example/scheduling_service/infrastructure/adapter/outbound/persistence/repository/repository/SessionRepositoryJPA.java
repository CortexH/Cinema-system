package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository;

import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepositoryJPA extends JpaRepository<SessionEntity, UUID> {
}
