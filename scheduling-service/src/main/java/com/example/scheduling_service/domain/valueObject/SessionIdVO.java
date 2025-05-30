package com.example.scheduling_service.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public record SessionIdVO(UUID value) {

    public SessionIdVO {
        Objects.requireNonNull(value, "Id da sala não pode ser nulo!");
    }

    public static SessionIdVO generate() {
        return new SessionIdVO(UUID.randomUUID());
    }

    public static SessionIdVO from(String id){
        Objects.requireNonNull(id, "Id não pode ser nulo!");
        try{
            return new SessionIdVO(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("formato inválido de string para UUID", e);
        }
    }

    public static SessionIdVO from(UUID id){
        return new SessionIdVO(id);
    }
}
