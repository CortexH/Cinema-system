package com.example.room_service.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity()
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomEntity {

    @Id
    private UUID id;

    @Column(nullable = false, name = "room_name")
    private String name;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "seats")
    private List<SeatEntity> seats;

    public void addSeat(SeatEntity seat){
        this.seats.add(seat);
    }

}
