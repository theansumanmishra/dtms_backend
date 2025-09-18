package com.disputetrackingsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dts_savings_accounts")
public class SavingsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonBackReference("client-savings")   // prevent infinite loop
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "account_number", nullable = false)
    private long accountNumber;

    @Column(nullable = false)
    private double balance;

    @Column(name = "account_creation_date")
    @CreationTimestamp
    private Date accountCreationDate;

    @Column(name = "is_blocked_for_credit")
    private boolean isBlockedForCredit;

    @Column(name = "is_blocked_for_debit")
    private boolean isBlockedForDebit;

    @OneToMany(mappedBy = "savingsAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("savings-debitcards")
    private List<DebitCard> debitCards = new ArrayList<>();
}
