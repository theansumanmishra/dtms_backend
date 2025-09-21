package com.disputetrackingsystem.model;

import com.disputetrackingsystem.security.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

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

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "reviewed_by", nullable = true)
    private User reviewedBy;

    private String Comments;

    // initiated, in_progress, closed
    @ManyToOne
    @JoinColumn(name = "status")
    private ConfigurableListDetails status;

    // accepted, partial-accepted, rejected
    @ManyToOne
    @JoinColumn(name = "sub_status")
    private ConfigurableListDetails subStatus;
}
