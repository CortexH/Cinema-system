package com.example.ticket_service.infrastructure.adapter.outbound.persistence.repository.repository;

import com.example.ticket_service.infrastructure.adapter.outbound.persistence.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringTicketRepository extends JpaRepository<TicketEntity, UUID> {
}
