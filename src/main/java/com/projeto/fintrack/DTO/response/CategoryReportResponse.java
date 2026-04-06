package com.projeto.fintrack.DTO.response;

import java.math.BigDecimal;

public record CategoryReportResponse(
        Long categoryId,
        String categoryName,
        String color,
        BigDecimal total,
        Double percentage
) {}