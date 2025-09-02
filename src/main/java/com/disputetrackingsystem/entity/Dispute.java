package com.disputetrackingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

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
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "payment_rail")
    private String paymentRail;

    @Column(name = "payment_rail_instance_id")
    private Integer paymentRailInstanceId;

    @Column(name = "dispute_source")
    private String disputeSource;

    @Column(name="dispute_summary")
    private String disputeSummary;

    @Column(name = "dispute_description")
    private String disputeDescription;

    @Column(name = "dispute_created_date")
    @CreationTimestamp
    private Date disputeCreatedDate;

    @ManyToOne
    @JoinColumn(name = "status")
    private ConfigurableListDetails status;

    @ManyToOne
    @JoinColumn(name = "sub_status")
    private ConfigurableListDetails subStatus;

}
