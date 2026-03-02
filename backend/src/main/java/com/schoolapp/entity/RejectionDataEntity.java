package com.schoolapp.entity;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rejection_data")
public class RejectionDataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "report_date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "batch_no")
	private String batchNo;

	@Column(name = "block_size")
	private String blockSize;

	private Integer qty;
	@Column(name = "shift")
	private String shift;

	@Column(name = "corner_damage")
	private Integer cornerDamage;

	@Column(name = "eruption_type")
	private Integer eruptionType;

	@Column(name = "top_side_damages")
	private Integer topSideDamages;

	@Column(name = "side_crack_thermal_crack")
	private Integer sideCrackThermalCrack;

	@Column(name = "rising_crack")
	private Integer risingCrack;

	@Column(name = "centre_crack")
	private Integer centreCrack;

	@Column(name = "bottom_uncut_blocks")
	private Integer bottomUncutBlocks;

	@Column(name = "total_breakages")
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

	@Column(name = "is_active")
	private Integer isActive;
}
