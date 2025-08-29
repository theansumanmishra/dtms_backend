package com.disputetrackingsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "dts_savings_account_transaction")
public class SavingsAccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "savings_account_id", nullable = false)
    private SavingsAccount savingsAccount;

    @ManyToOne
    @JoinColumn(name = "debit_card_id", nullable = false)
    private DebitCard debitCard;

    private Double amount;

    @Column(name = "account_created")
    private Timestamp accountCreated;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_sub_type")
    private String transactionSubType;

    public SavingsAccountTransaction(SavingsAccount savingsAccount, DebitCard debitCard, Double amount, Timestamp accountCreated, String transactionType, String transactionSubType) {
        this.savingsAccount = savingsAccount;
        this.debitCard = debitCard;
        this.amount = amount;
        this.accountCreated = accountCreated;
        this.transactionType = transactionType;
        this.transactionSubType = transactionSubType;
    }
}
