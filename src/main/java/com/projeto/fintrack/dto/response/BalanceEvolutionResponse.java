package com.projeto.fintrack.dto.response;

import java.math.BigDecimal;

public record BalanceEvolutionResponse(
        int year,
        int month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balance
) {}

