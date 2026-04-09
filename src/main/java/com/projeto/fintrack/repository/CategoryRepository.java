package com.projeto.fintrack.repository;

import com.projeto.fintrack.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            SELECT c FROM Category c
            WHERE c.active = true
            AND (c.user IS NULL OR c.user.id = :userId)
            ORDER BY CASE WHEN c.user IS NULL THEN 0 ELSE 1 END, c.name
            """)
    List<Category> findAllAvailableForUser(@Param("userId") Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COUNT(c) > 0 FROM Category c
            WHERE c.name = :name
            AND c.user.id = :userId
            """)
    boolean existsByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);

    @Query("""
            SELECT COUNT(t) > 0
            FROM Transaction t
            WHERE t.category.id = :categoryId
            """)
    boolean hasTransactions(@Param("categoryId") Long categoryId);

    @Query("""
            SELECT c FROM Category c
            WHERE c.id = :id
            AND c.active = true
            AND (c.user IS NULL OR c.user.id = :userId)
            """)
    Optional<Category> findAvailableByIdForUser(@Param("id") Long id, @Param("userId") Long userId);
}