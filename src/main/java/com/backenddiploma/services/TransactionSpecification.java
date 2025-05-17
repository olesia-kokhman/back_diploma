package com.backenddiploma.services;

import com.backenddiploma.models.Transaction;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> filterByUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }
            Join<Object, Object> userJoin = root.join("user", JoinType.INNER);
            return cb.equal(userJoin.get("id"), userId);
        };
    }

    public static Specification<Transaction> filterByCategoryIds(List<Long> categoryIds) {
        return (root, query, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
            return categoryJoin.get("id").in(categoryIds);
        };
    }

    public static Specification<Transaction> filterByAccountIds(List<Long> accountIds) {
        return (root, query, cb) -> {
            if (accountIds == null || accountIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Object, Object> accountJoin = root.join("account", JoinType.LEFT);
            return accountJoin.get("id").in(accountIds);
        };
    }

    public static Specification<Transaction> filterByDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);
                return cb.between(root.get("dateAndTime"), startDateTime, endDateTime);
            } else if (startDate != null) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                return cb.greaterThanOrEqualTo(root.get("dateAndTime"), startDateTime);
            } else if (endDate != null) {
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);
                return cb.lessThanOrEqualTo(root.get("dateAndTime"), endDateTime);
            } else {
                return cb.conjunction();
            }
        };
    }

    public static Specification<Transaction> containsKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction();
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";
            query.distinct(true);

            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
            Join<Object, Object> accountJoin = root.join("account", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("description")), likePattern),
                    cb.like(cb.lower(root.get("amount").as(String.class)), likePattern),
                    cb.like(cb.lower(root.get("dateAndTime").as(String.class)), likePattern),
                    cb.like(cb.lower(categoryJoin.get("name")), likePattern),
                    cb.like(cb.lower(accountJoin.get("name")), likePattern)
            );
        };
    }
}
