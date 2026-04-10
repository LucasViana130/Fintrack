package com.projeto.fintrack.dto.response;

import com.projeto.fintrack.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String name,
        AccountType type,
        String description,
        Boolean active,
        BigDecimal balance,
        LocalDateTime createdAt
) {}
