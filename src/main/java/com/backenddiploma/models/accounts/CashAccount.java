package com.backenddiploma.models.accounts;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cash_accounts")
@DiscriminatorValue("CASH")
@Data
@NoArgsConstructor
public class CashAccount extends Account {
}
