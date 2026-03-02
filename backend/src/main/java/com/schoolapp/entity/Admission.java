package com.schoolapp.entity;

// import java.io.File;
import java.sql.Date;
// import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int admissionId;
	private int classId;
	private String mobileNo1;
	private String mobileNo2;
	private String classStandard;
	private String firstName;
	private String middleName;
	private String lastName;
	private String motherName;
	private String gender;
	private Date dob;
	private int age;
	private String birthPlace;
	private String nationality;
	private String motherTongue;
	private String religion;
	private int rte;
	private int cast;
	private int subCast;
	private int category;
	private String bus;
	private String teacherRelative;
	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private String regId;
	private String uDiskId;
	private String GRno;
	private String stdSaralPortalId;
	private int pickUpPoint;
	private String adharNo;
	// private File file;

}
