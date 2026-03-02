package com.schoolapp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "wire_cutting_report")
public class WireCuttingReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String batchNo;

	@Temporal(TemporalType.DATE)
	private Date cuttingDate;

	private int mouldNo;
	private int size;
	private int ballTestMm;

	private String otherReason;
	private String time;

	private String shift; // Morning / Afternoon / Night

	// ===== APPROVAL FLOW =====
	private String approvalStage; // L1, L2, L3, APPROVED, REJECTED

	private String approvedByL1;
	private String approvedByL2;
	private String approvedByL3;

	private String rejectionReason;

	// ===== SYSTEM =====
	private int userId;
	private int branchId;
	private int orgId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	private int updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	private int isActive;

	@PrePersist
	public void onCreate() {
		createdDate = new Date();
		isActive = 1;
	}

	@PreUpdate
	public void onUpdate() {
		updatedDate = new Date();
	}
}
