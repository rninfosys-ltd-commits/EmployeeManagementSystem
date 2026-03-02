package com.schoolapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "autoclave_cycle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoclaveCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String autoclaveNo;
    private String runNo;
    private String startedAt;
    private Date startedDate;
    private String completedAt;
    private Date completedDate;

    private String remarks;

    private String shift; // Morning / Afternoon / Night

    // 🔥 REQUIRED FIELDS
    private int userId;
    private int branchId;
    private int orgId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private int updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private int isActive = 1;

    // 🔗 RELATION
    @OneToMany(mappedBy = "autoclave", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AutoclaveWagon> wagons;

}
