package com.example.room_service.infrastructure.adapter.outbound.persistence.entity;

import com.example.room_service.domain.enums.SeatState;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "seat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatEntity {

    @Id
    private UUID id;

    @Column(nullable = false, name = "seat_number")
    private String seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatState state;

    @Column(nullable = false)
    private Boolean blocked;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RoomEntity roomEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatEntity that = (SeatEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : getClass().hashCode();
    }

}
