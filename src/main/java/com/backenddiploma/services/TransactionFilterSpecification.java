package com.backenddiploma.services;

import com.backenddiploma.models.Transaction;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class TransactionFilterSpecification {

    public static Specification<Transaction> filterByCategoryIds(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
            return categoryJoin.get("id").in(categoryIds);
        };
    }

    public static Specification<Transaction> filterByAccountIds(List<Long> accountIds) {
        return (root, query, criteriaBuilder) -> {
            if (accountIds == null || accountIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Object, Object> accountJoin = root.join("account", JoinType.LEFT);
            return accountJoin.get("id").in(accountIds);
        };
    }

    public static Specification<Transaction> filterByDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("dateTime"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), startDate);
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dateTime"), endDate);
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }

}
