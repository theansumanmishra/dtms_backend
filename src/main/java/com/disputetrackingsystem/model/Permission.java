package com.disputetrackingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "dts_permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonIgnore                             // 👈 prevents infinite recursion
    @ManyToMany(mappedBy = "permissions")  // "permissions" is the field name in Role entity
    private Set<Role> roles = new HashSet<>();
}
