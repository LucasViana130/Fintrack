package com.projeto.fintrack.dto.request;

import com.projeto.fintrack.domain.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotNull(message = "Tipo é obrigatório")
        TransactionType type,

        String color
) {}
