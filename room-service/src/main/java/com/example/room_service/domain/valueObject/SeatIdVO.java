package com.example.room_service.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public record SeatIdVO(UUID value) {

    public SeatIdVO {
        Objects.requireNonNull(value, "Id da poltrona não pode ser nulo!");
    }

    public static SeatIdVO generate(){
        return new SeatIdVO(UUID.randomUUID());
    }

    public static SeatIdVO from(String id){
        Objects.requireNonNull(id, "'id' não pode ser nulo!");
        try{
            return new SeatIdVO(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("formato inválido de string para UUID", e);
        }
    }

    public static SeatIdVO from(UUID id){
        return new SeatIdVO(id);
    }

}
