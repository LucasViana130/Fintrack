package com.projeto.fintrack.dto.response;

import com.projeto.fintrack.domain.enums.TransactionType;

public record CategoryResponse(
        Long id,
        String name,
        TransactionType type,
        String color,
        Boolean active,
        Boolean isDefault
) {}
