package com.example.room_service.infrastructure.adapter.outbound.persistence.repository.repository;

import com.example.room_service.infrastructure.adapter.outbound.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringRoomRepository extends JpaRepository<RoomEntity, UUID> {

}
