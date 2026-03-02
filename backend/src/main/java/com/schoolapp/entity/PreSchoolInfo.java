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
public class PreSchoolInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int preSchoolInfoId;
	private int schoolId;
	private String nameOfPreviousSchool;
	private int stdOfPreviousSchool;
	private String lastYearResult;
	private Date dateOfAdmission;
	private int admissionStd;

	private int division;
	private int sId;
	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}
