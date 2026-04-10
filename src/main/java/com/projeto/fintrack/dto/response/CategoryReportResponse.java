package com.projeto.fintrack.dto.response;

import java.math.BigDecimal;

public record CategoryReportResponse(
        Long categoryId,
        String categoryName,
        String color,
        BigDecimal total,
        Double percentage
) {}