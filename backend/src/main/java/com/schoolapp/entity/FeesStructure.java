package com.schoolapp.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeesStructure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int feesStructureId;
	private int classId;
	private int reasourceId;
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
