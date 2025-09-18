package com.disputetrackingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "dts_dispute_transactions")
public class DisputeTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "dispute_id", nullable = false)
    private Dispute dispute;

    @ManyToOne
    @JoinColumn(name = "savings_account_transaction_id", nullable = false)
    private SavingsAccountTransaction savingsAccountTransaction;

    @Column(name = "dispute_entry_date",nullable = false)
    @CreationTimestamp
    private Date disputeEntryDate;

    @Column(name = "disputed_amount",precision = 12, scale = 2, nullable = false)
    private BigDecimal disputedAmount;

    @ManyToOne
    @JoinColumn(name = "status")
    private ConfigurableListDetails status;

    @ManyToOne
    @JoinColumn(name = "sub_status")
    private ConfigurableListDetails subStatus;
}
