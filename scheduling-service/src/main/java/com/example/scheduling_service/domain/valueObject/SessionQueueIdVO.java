package com.example.scheduling_service.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public record SessionQueueIdVO(UUID value) {

    public SessionQueueIdVO {
        Objects.requireNonNull(value, "Id da sala não pode ser nulo!");
    }

    public static SessionQueueIdVO generate() {
        return new SessionQueueIdVO(UUID.randomUUID());
    }

    public static SessionQueueIdVO from(String id){
        Objects.requireNonNull(id, "Id não pode ser nulo!");
        try{
            return new SessionQueueIdVO(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("formato inválido de string para UUID", e);
        }
    }

    public static SessionQueueIdVO from(UUID id){
        return new SessionQueueIdVO(id);
    }
}
