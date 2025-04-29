package com.backenddiploma.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToMany
    @JoinTable(
            name = "transaction_category",
            joinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id") )
    private List<Category> category = new ArrayList<>();

    private String description;

    @Column(name = "date_and_time")
    private LocalDateTime dateTime;

}
