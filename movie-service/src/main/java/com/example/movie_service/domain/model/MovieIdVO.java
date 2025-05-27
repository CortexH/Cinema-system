package com.example.movie_service.domain.model;

import java.util.Objects;
import java.util.UUID;

public record MovieIdVO(UUID value) {

    public static MovieIdVO from(String uuid){
        Objects.requireNonNull(uuid, "Id não pode ser nulo!");
        try{
            return new MovieIdVO(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("formato inválido de string para UUID", e);
        }
    }

    public static MovieIdVO generate(){
        return new MovieIdVO(UUID.randomUUID());
    }



}
