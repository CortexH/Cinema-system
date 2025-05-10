package com.example.ticket_service.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity {

    @Id
    private UUID id;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "seat_id")
    private String seatId;

    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    private String movie;

    private Boolean accessibility;

    @Column(name = "movie_time")
    private LocalDateTime movieTime;

    private Boolean valid;

    @Column(name = "in_use")
    private Boolean inUse;

}
