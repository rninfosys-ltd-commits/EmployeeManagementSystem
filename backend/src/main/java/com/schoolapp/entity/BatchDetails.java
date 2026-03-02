package com.schoolapp.entity;

import jakarta.persistence.*;
//import lombok.*;

//import lombok.*;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Table(name = "batch_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bactno;

	private Date trndate;
	private String createdby;

	private String aproval1;
	private String aproval2;
	private String aproval3;
	private String aproval4;

	// --- Approver Names (Not stored in DB, only for frontend response) ---
	@Transient
	private String aproval1Name;

	@Transient
	private String aproval2Name;

	@Transient
	private String aproval3Name;

	// 🔥 NEW FIELD FOR WORKFLOW STAGE
	// NONE → L1 → L2 → L3
	@Column(name = "approval_stage")
	@Getter(AccessLevel.NONE)
	private String approvalStage = "NONE";

	@OneToMany(mappedBy = "batchDetails", cascade = CascadeType.ALL)
	@com.fasterxml.jackson.annotation.JsonIgnore
	private List<CustomerTrnDetails> transactions;

	public String getApprovalStage() {
		return (approvalStage == null || approvalStage.isEmpty()) ? "NONE" : approvalStage;
	}

}
