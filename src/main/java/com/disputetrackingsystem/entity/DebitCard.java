package com.disputetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "dts_debit_cards")
public class DebitCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_no", unique = true, nullable = false)
    private Long cardNo;

    @ManyToOne
    @JoinColumn(name = "savings_account_id", nullable = false)
    private SavingsAccount savingsAccount;

    @Column(name = "debit_card_issued_date")
    private Timestamp debitCardIssuedDate;

    @Column(name = "expiry_month")
    private Integer expiryMonth;

    @Column(name = "expiry_year")
    private Integer expiryYear;

    @Column(name = "cvv_no", unique = true)
    private Integer cvvNo;

    @Column(name = "is_blocked")
    private boolean isBlocked;

}
