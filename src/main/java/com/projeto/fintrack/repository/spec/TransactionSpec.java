package com.projeto.fintrack.repository.spec;

import com.projeto.fintrack.domain.entity.Transaction;
import com.projeto.fintrack.domain.enums.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpec {

    public static Specification<Transaction> filter(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            TransactionType type,
            Long categoryId,
            Long accountId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("user").get("id"), userId));

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
            }
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (accountId != null) {
                predicates.add(cb.equal(root.get("account").get("id"), accountId));
            }

            query.orderBy(
                    cb.desc(root.get("date")),
                    cb.desc(root.get("createdAt"))
            );

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}