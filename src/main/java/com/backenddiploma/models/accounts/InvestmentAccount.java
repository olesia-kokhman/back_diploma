package com.backenddiploma.models.accounts;

import com.backenddiploma.models.accounts.Account;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("INVESTMENT")
@Table(name = "investment_accounts")
@Data
@NoArgsConstructor
public class InvestmentAccount extends Account {

    @Column(nullable = false)
    private double quantity;

    @Column(name = "buy_price", nullable = false)
    private double buyPrice;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "buy_date", nullable = false)
    private LocalDate buyDate;

    @Column(length = 255)
    private String platform;

}
