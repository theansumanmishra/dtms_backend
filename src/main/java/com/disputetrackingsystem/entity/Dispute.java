package com.disputetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.springframework.data.domain.Persistable;

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
    @JoinColumn(name = "transaction_id", nullable = false)
    private SavingsAccountTransaction savingsAccountTransaction;

    // Email, branch visit, website
    @Column(name = "source", nullable = false)
    private String source;

    @Column(name="reason", nullable = false)
    private String reason;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    // initiated, in_progress, closed
    @ManyToOne
    @JoinColumn(name = "status")
    private ConfigurableListDetails status;

    // accepted, partial-accepted, rejected
    @ManyToOne
    @JoinColumn(name = "sub_status")
    private ConfigurableListDetails subStatus;
}


