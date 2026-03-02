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
public class StudentInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int studentInfoId;
	private String fatherName;
	private String motherName;
	private String fatherOccupation;
	private String motherOccupation;
	private String officeAddress;
	private int officeContact;
	private String currentAddress;
	private String permanentAddress;
	private int homeContact;
	private int mobileNo;
	private int mothlyIncome;
	@jakarta.persistence.Column(name = "s_id")
	private int sid;

	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}
