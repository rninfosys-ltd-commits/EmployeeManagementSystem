package com.schoolapp.entity;

//package com.yourapp.blockseparating.entity;

import jakarta.persistence.*;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block_separating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockSeparating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== FORM FIELDS =====
    @Column(nullable = false)
    private String batchNumber;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date castingDate;

    @Column(nullable = false)
    private String blockSize;

    @Column(nullable = false)
    private Integer time;

    // 🔥 ADD THIS
    @Column(name = "shift", length = 1, nullable = false)
    private String shift; // 1, 2, 3, G
    private String remark;

    @Temporal(TemporalType.DATE)
    @Column(name = "report_date", nullable = false)
    private Date reportDate;

    // ===== COMMON REQUIRED FIELDS =====
    private int userId;
    private int branchId;
    private int orgId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private int updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    private int isActive;

}
