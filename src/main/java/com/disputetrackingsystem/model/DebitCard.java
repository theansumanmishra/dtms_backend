package com.disputetrackingsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "dts_debit_cards")
public class DebitCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "savings_account_id", nullable = false)
    @JsonBackReference("savings-debitcards")
    private SavingsAccount savingsAccount;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "card_no", unique = true, nullable = false)
    private Long cardNo;

    @Column(name = "debit_card_issued_date", nullable = false)
    @CreationTimestamp
    private Date debitCardIssuedDate;

    @Column(name = "expiry_month", nullable = false)
    private Integer expiryMonth;

    @Column(name = "expiry_year", nullable = false)
    private Integer expiryYear;

    @Column(name = "cvv_no", unique = true, nullable = false)
    private Integer cvvNo;

    @Column(nullable = false)
    private String pin;

    @Column(name = "is_blocked")
    private boolean isBlocked;
}
