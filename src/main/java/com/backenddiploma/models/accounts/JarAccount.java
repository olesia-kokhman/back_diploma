package com.backenddiploma.models.accounts;

import com.backenddiploma.models.accounts.Account;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jar_accounts")
@DiscriminatorValue("JAR")
@Data
@NoArgsConstructor
public class JarAccount extends Account {

    @Column(nullable = false)
    private double goal;
}
