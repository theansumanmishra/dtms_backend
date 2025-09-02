package com.disputetrackingsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dts_savings_accounts")
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private int balance;

    @Column(name = "account_creation_date")
    private Timestamp accountCreationDate;

    @Column(name = "is_blocked_for_credit")
    private boolean isBlockedForCredit;

    @Column(name = "is_blocked_for_debit")
    private boolean isBlockedForDebit;
}
