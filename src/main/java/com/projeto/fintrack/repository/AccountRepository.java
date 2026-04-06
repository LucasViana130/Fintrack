package com.projeto.fintrack.repository;

import com.projeto.fintrack.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserIdAndActiveTrue(Long userId);

    Optional<Account> findByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserIdAndActiveTrue(String name, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END), 0)
            FROM Transaction t
            WHERE t.account.id = :accountId
            """)
    BigDecimal calculateBalance(@Param("accountId") Long accountId);

    @Query("""
            SELECT COUNT(t) > 0
            FROM Transaction t
            WHERE t.account.id = :accountId
            """)
    boolean hasTransactions(@Param("accountId") Long accountId);
}