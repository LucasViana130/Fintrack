package com.projeto.fintrack.dto.request;

import com.projeto.fintrack.domain.enums.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotNull(message = "Conta é obrigatória")
        Long accountId,

        @NotNull(message = "Categoria é obrigatória")
        Long categoryId,

        @NotNull(message = "Tipo é obrigatório")
        TransactionType type,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal amount,

        String description,

        @NotNull(message = "Data é obrigatória")
        @PastOrPresent(message = "A data não pode ser futura")
        LocalDate date
) {}
