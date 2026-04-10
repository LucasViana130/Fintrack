package com.projeto.fintrack.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        String name,

        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String password
) {}

