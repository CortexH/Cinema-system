package com.example.room_service.domain.valueObject;


import java.util.Objects;
import java.util.UUID;

public record RoomIdVO(UUID value) {

    public RoomIdVO {
        Objects.requireNonNull(value, "Id da sala não pode ser nulo!");
    }

    public static RoomIdVO generate() {
        return new RoomIdVO(UUID.randomUUID());
    }

    public static RoomIdVO from(String id){
        Objects.requireNonNull(id, "Id não pode ser nulo!");
        try{
            return new RoomIdVO(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("formato inválido de string para UUID", e);
        }
    }

    public static RoomIdVO from(UUID id){
        return new RoomIdVO(id);
    }
}
