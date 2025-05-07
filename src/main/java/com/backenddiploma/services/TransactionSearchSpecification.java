package com.backenddiploma.services;

import com.backenddiploma.models.Transaction;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSearchSpecification {

    public static Specification<Transaction> containsKeyword(String keyword) {

        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("amount").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("dateTime").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("category", JoinType.LEFT).get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("account", JoinType.LEFT).get("name")), likePattern)
            );
        };
    }

}
