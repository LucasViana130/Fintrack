package com.projeto.fintrack.controller;

import com.projeto.fintrack.dto.response.BalanceEvolutionResponse;
import com.projeto.fintrack.dto.response.CategoryReportResponse;
import com.projeto.fintrack.dto.response.MonthlySummaryResponse;
import com.projeto.fintrack.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios financeiros")
@SecurityRequirement(name = "Bearer Authentication")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly-summary")
    @Operation(summary = "Resumo financeiro mensal (receitas, despesas e saldo)")
    public MonthlySummaryResponse getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month) {
        return reportService.getMonthlySummary(year, month);
    }

    @GetMapping("/expenses-by-category")
    @Operation(summary = "Gastos agrupados por categoria no período")
    public List<CategoryReportResponse> getExpensesByCategory(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return reportService.getExpensesByCategory(startDate, endDate);
    }

    @GetMapping("/balance-evolution")
    @Operation(summary = "Evolução do saldo nos últimos 12 meses")
    public List<BalanceEvolutionResponse> getBalanceEvolution() {
        return reportService.getBalanceEvolution();
    }
}