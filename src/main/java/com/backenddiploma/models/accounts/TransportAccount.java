package com.backenddiploma.models.accounts;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transport_accounts")
@DiscriminatorValue("TRANSPORT")
@Data
@NoArgsConstructor
public class TransportAccount extends Account {
}
