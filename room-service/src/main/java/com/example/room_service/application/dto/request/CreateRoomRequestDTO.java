package com.example.room_service.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRoomRequestDTO(

        @NotBlank(message = "o campo 'name' é obrigatório!")
        String name,

        @NotNull(message = "o campo 'rows' é obrigatório!")
        @Min(value = 1, message = "'rows' não pode ser menor que 1!")
        Integer rows,

        @NotNull(message = "o campo 'seats_per_rows' é obrigatório!")
        @Min(value = 1, message = "'seats_per_rows' não pode ser menor que 1!")
        Integer seats_per_rows

) {
}
