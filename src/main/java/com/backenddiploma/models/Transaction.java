package com.backenddiploma.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private double amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String description;

    @Column(name = "date_and_time")
    private LocalDateTime dateTime;

}
