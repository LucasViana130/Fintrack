package com.projeto.fintrack.DTO.response;

import com.projeto.fintrack.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        TransactionType type,
        BigDecimal amount,
        String description,
        LocalDate date,
        AccountResponse account,
        CategoryResponse category,
        LocalDateTime createdAt
) {}
