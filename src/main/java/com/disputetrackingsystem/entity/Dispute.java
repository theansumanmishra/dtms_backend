package com.disputetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;

import java.sql.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "dts_disputes")
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private SavingsAccountTransaction transaction;

    // Email, branch visit, website
    @Column(name = "source", nullable = false)
    private String disputeSource;

    @Column(name="reason", nullable = false)
    private String disputeReason;

    @Column(name = "description", nullable = false)
    private String disputeDescription;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Date disputeCreatedDate;

    // initiated, in_progress, closed
    @ManyToOne
    @JoinColumn(name = "status")
    private ConfigurableListDetails status;

    // accepted, partial-accepted, rejected
    @ManyToOne
    @JoinColumn(name = "sub_status")
    private ConfigurableListDetails subStatus;
}

