package com.schoolapp.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "material_master")
public class MaterialMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String materialName;

    @Column(length = 20)
    private String unit; // Kg, Liter, gm, °C, etc.

    @Builder.Default
    private int displayOrder = 0;

    @Builder.Default
    private int isActive = 1;

    private int orgId;
    private int branchId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @PrePersist
    public void onCreate() {
        this.createdDate = new Date();
        this.isActive = 1;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedDate = new Date();
    }
}
