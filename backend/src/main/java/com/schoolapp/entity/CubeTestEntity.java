package com.schoolapp.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cube_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CubeTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "cast_date")
    @Temporal(TemporalType.DATE)
    private Date castDate;

    @Column(name = "testing_date")
    @Temporal(TemporalType.DATE)
    private Date testingDate;

    @Column(name = "shift")
    private String shift;

    @Column(name = "cube_dimension_immediate")
    private String cubeDimensionImmediate;

    @Column(name = "cube_dimension_over_dry")
    private String cubeDimensionOverDry;

    @Column(name = "weight_immediate_kg")
    private String weightImmediateKg;

    @Column(name = "weight_over_dry_kg")
    private String weightOverDryKg;

    @Column(name = "weight_with_moisture_kg")
    private Double weightWithMoistureKg;

    @Column(name = "load_over_dry_tonn")
    private Double loadOverDryTonn;

    @Column(name = "load_moisture_tonn")
    private Double loadMoistureTonn;

    @Column(name = "comp_strength_over_dry")
    private Double compStrengthOverDry;

    @Column(name = "comp_strength_moisture")
    private Double compStrengthMoisture;

    @Column(name = "density_kgm3")
    private Double densityKgM3;

    // ---------- APPROVAL WORKFLOW ----------

    @Column(name = "approved_by_l1")
    private String approvedByL1;

    @Column(name = "approved_by_l2")
    private String approvedByL2;

    @Column(name = "approved_by_l3")
    private String approvedByL3;

    // @Column(length = 20)
    @Column(name = "approval_stage")
    private String approvalStage = "NONE";

    @Column(name = "rejected_by")
    private String rejectedBy;

    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "branch_id")
    private int branchId;

    @Column(name = "org_id")
    private int orgId;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_by")
    private int updatedBy;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "is_active")
    private int isActive;

}
