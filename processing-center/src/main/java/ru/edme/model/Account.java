package ru.edme.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 50)
    private String accountNumber;
    private BigDecimal balance;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private Currency currency;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private IssuingBank issuingBank;
}
