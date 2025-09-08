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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dts_disputes")
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

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

    // Email, branch visit, website
    @Column(name = "dispute_source", nullable = false)
    private String disputeSource;

    @Column(name="dispute_summary", nullable = false)
    private String disputeSummary;

    @Column(name = "dispute_description", nullable = false)
    private String disputeDescription;

    @Column(name = "dispute_created_date", nullable = false)
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
