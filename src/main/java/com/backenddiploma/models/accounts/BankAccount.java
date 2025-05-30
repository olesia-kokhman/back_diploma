package com.backenddiploma.models.accounts;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_accounts")
@DiscriminatorValue("BANK_ACCOUNT")
@Data
@NoArgsConstructor
public class BankAccount extends Account {
}
