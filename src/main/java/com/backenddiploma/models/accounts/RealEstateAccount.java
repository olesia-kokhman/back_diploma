package com.backenddiploma.models.accounts;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "real_estate_accounts")
@DiscriminatorValue("REAL_ESTATE")
@Data
@NoArgsConstructor
public class RealEstateAccount extends Account {
}
