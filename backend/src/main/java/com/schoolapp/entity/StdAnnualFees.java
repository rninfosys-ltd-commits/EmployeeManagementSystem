package com.schoolapp.entity;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class StdAnnualFees {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int stdAnnualFeesId;
	private int classId;
	private int reasourceId;
	@jakarta.persistence.Column(name = "s_id")
	private int sid;
	private int feesStructureId;
	private int financeYear;
	private int charges;
	private Date trDate;
	private int isActive;
	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;

	@jakarta.persistence.ManyToOne
	@jakarta.persistence.JoinColumn(name = "classId", insertable = false, updatable = false)
	private ClassMaster classMaster;

	@jakarta.persistence.ManyToOne
	@jakarta.persistence.JoinColumn(name = "reasourceId", insertable = false, updatable = false)
	private ReasourceMaster reasourceMaster;
}
