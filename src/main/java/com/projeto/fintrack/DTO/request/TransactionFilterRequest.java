package com.projeto.fintrack.DTO.request;

import com.projeto.fintrack.domain.enums.TransactionType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TransactionFilterRequest(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        TransactionType type,
        Long categoryId,
        Long accountId,
        Integer page,
        Integer size
) {
    public int pageNumber() {
        return page != null ? page : 0;
    }

    public int pageSize() {
        return size != null ? size : 10;
    }
}