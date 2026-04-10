package com.projeto.fintrack.service;

import com.projeto.fintrack.dto.response.BalanceEvolutionResponse;
import com.projeto.fintrack.dto.response.CategoryReportResponse;
import com.projeto.fintrack.dto.response.MonthlySummaryResponse;
import com.projeto.fintrack.domain.entity.User;
import com.projeto.fintrack.domain.enums.TransactionType;
import com.projeto.fintrack.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public MonthlySummaryResponse getMonthlySummary(int year, int month) {
        User user = userService.getAuthenticatedUser();

        BigDecimal income = transactionRepository.sumByUserAndTypeAndYearAndMonth(
                user.getId(), TransactionType.INCOME, year, month);

        BigDecimal expense = transactionRepository.sumByUserAndTypeAndYearAndMonth(
                user.getId(), TransactionType.EXPENSE, year, month);

        BigDecimal balance = income.subtract(expense);

        return new MonthlySummaryResponse(year, month, income, expense, balance);
    }

    @Transactional(readOnly = true)
    public List<CategoryReportResponse> getExpensesByCategory(LocalDate startDate, LocalDate endDate) {
        User user = userService.getAuthenticatedUser();

        List<Object[]> results = transactionRepository.sumExpensesByCategory(
                user.getId(), startDate, endDate);

        BigDecimal totalExpenses = results.stream()
                .map(row -> (BigDecimal) row[3])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return results.stream()
                .map(row -> {
                    Long categoryId = (Long) row[0];
                    String categoryName = (String) row[1];
                    String color = (String) row[2];
                    BigDecimal total = (BigDecimal) row[3];

                    double percentage = totalExpenses.compareTo(BigDecimal.ZERO) == 0
                            ? 0
                            : total.divide(totalExpenses, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue();

                    return new CategoryReportResponse(categoryId, categoryName, color, total, percentage);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BalanceEvolutionResponse> getBalanceEvolution() {
        User user = userService.getAuthenticatedUser();

        LocalDate startDate = LocalDate.now().minusMonths(11).withDayOfMonth(1);

        List<Object[]> results = transactionRepository.findMonthlyEvolution(user.getId(), startDate);

        return results.stream()
                .map(row -> {
                    int year = ((Number) row[0]).intValue();
                    int month = ((Number) row[1]).intValue();
                    BigDecimal income = (BigDecimal) row[2];
                    BigDecimal expense = (BigDecimal) row[3];
                    BigDecimal balance = income.subtract(expense);
                    return new BalanceEvolutionResponse(year, month, income, expense, balance);
                })
                .toList();
    }
}
