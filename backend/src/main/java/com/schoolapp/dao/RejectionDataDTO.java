package com.schoolapp.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RejectionDataDTO {

	private Long id;
	private Date date;
	private String batchNo;
	private String blockSize;
	private Integer qty;
	private String shift;

	private Integer cornerDamage;
	private Integer eruptionType;
	private Integer topSideDamages;
	private Integer sideCrackThermalCrack;
	private Integer risingCrack;
	private Integer centreCrack;
	private Integer bottomUncutBlocks;
	private Integer totalBreakages;
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	private Integer isActive;
}
