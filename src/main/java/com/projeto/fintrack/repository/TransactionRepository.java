package com.projeto.fintrack.repository;

import com.projeto.fintrack.domain.entity.Transaction;
import com.projeto.fintrack.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
            WHERE t.user.id = :userId
            AND t.type = :type
            AND EXTRACT(YEAR FROM t.date) = :year
            AND EXTRACT(MONTH FROM t.date) = :month
            """)
    BigDecimal sumByUserAndTypeAndYearAndMonth(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("""
            SELECT t.category.id, t.category.name, t.category.color, COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.id = :userId
            AND t.type = 'EXPENSE'
            AND t.date BETWEEN :startDate AND :endDate
            GROUP BY t.category.id, t.category.name, t.category.color
            ORDER BY SUM(t.amount) DESC
            """)
    List<Object[]> sumExpensesByCategory(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date),
                   COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0),
                   COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0)
            FROM Transaction t
            WHERE t.user.id = :userId
            AND t.date >= :startDate
            GROUP BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date)
            ORDER BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date)
            """)
    List<Object[]> findMonthlyEvolution(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate
    );
}