package com.projeto.fintrack.dto.request;

import com.projeto.fintrack.domain.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotNull(message = "Tipo é obrigatório")
        AccountType type,

        String description
) {}