package com.disputetrackingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "dts_savings_account_transactions")
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

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    // e.g: UPI, card, NEFT
    @Column(name = "payment_rail", nullable = false)
    private String paymentRail;

    // ref id
    @Generated
    @Column(name = "payment_rail_instance_id", nullable = false,
            updatable = false,
            insertable = false,
            columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID paymentRailInstanceId;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_category")
    private String merchantCategory;

    //e.g: credit,debit
    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    //e.g: ATM,POS
    @Column(name = "transaction_mode", nullable = false)
    private String transactionMode;

    @Column(nullable = false)
    private boolean disputable;
}
