package com.projeto.fintrack.repository;

import com.projeto.fintrack.domain.entity.Transaction;
import com.projeto.fintrack.domain.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT t FROM Transaction t
            WHERE t.user.id = :userId
            AND (:startDate IS NULL OR t.date >= :startDate)
            AND (:endDate IS NULL OR t.date <= :endDate)
            AND (:type IS NULL OR t.type = :type)
            AND (:categoryId IS NULL OR t.category.id = :categoryId)
            AND (:accountId IS NULL OR t.account.id = :accountId)
            ORDER BY t.date DESC, t.createdAt DESC
            """)
    Page<Transaction> findWithFilters(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("type") TransactionType type,
            @Param("categoryId") Long categoryId,
            @Param("accountId") Long accountId,
            Pageable pageable
    );

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
            WHERE t.user.id = :userId
            AND t.type = :type
            AND YEAR(t.date) = :year
            AND MONTH(t.date) = :month
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
            SELECT YEAR(t.date), MONTH(t.date),
                   COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0),
                   COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0)
            FROM Transaction t
            WHERE t.user.id = :userId
            AND t.date >= :startDate
            GROUP BY YEAR(t.date), MONTH(t.date)
            ORDER BY YEAR(t.date), MONTH(t.date)
            """)
    List<Object[]> findMonthlyEvolution(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate
    );
}
