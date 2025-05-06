package com.backenddiploma.logic;

import com.backenddiploma.models.Transaction;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TransactionSort {

    private static final Map<String, Function<Boolean, Specification<Transaction>>> sortMap = new HashMap<>();

    static {
        sortMap.put("description", asc -> (root, query, criteriaBuilder) -> {
            query.orderBy(asc ? criteriaBuilder.asc(root.get("description")) : criteriaBuilder.desc(root.get("description")));
            return criteriaBuilder.conjunction();
        });

        sortMap.put("amount", asc -> (root, query, criteriaBuilder) -> {
            query.orderBy(asc ? criteriaBuilder.asc(root.get("amount")) : criteriaBuilder.desc(root.get("amount")));
            return criteriaBuilder.conjunction();
        });

        sortMap.put("date", asc -> (root, query, criteriaBuilder) -> {
            query.orderBy(asc ? criteriaBuilder.asc(root.get("date")) : criteriaBuilder.desc(root.get("date")));
            return criteriaBuilder.conjunction();
        });

        sortMap.put("category", asc -> (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Object, Object> join = root.join("category", JoinType.LEFT);
            query.orderBy(asc ? criteriaBuilder.asc(join.get("name")) : criteriaBuilder.desc(join.get("name")));
            return criteriaBuilder.conjunction();
        });

        sortMap.put("account", asc -> (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Object, Object> join = root.join("account", JoinType.LEFT);
            query.orderBy(asc ? criteriaBuilder.asc(join.get("name")) : criteriaBuilder.desc(join.get("name")));
            return criteriaBuilder.conjunction();
        });

    }

    public static Specification<Transaction> getSortedTransactions(String sortBy, boolean direction) {
        return sortMap.getOrDefault(sortBy.toLowerCase(), sortMap.get("date")).apply(direction);
    }


}
