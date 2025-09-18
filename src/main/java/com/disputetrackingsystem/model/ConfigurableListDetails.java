package com.disputetrackingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dts_configurable_list_details")
public class ConfigurableListDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "configurable_list_id")
    private ConfigurableList configurableList;

    private String name;

    private String description;

}
