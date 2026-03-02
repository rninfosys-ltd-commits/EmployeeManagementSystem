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
public class AttendanceDetailes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int attendanceId;
	private int sId;
	private int teacherId;
	private int subjectId;
	private int classId;
	private int divId;
	private String time;
	private int year;
	private String month;

	private int d1;
	private int d2;
	private int d3;
	private int d4;
	private int d5;
	private int d6;
	private int d7;
	private int d8;
	private int d9;
	private int d10;
	private int d11;
	private int d12;
	private int d13;
	private int d14;
	private int d15;
	private int d16;
	private int d17;
	private int d18;
	private int d19;
	private int d20;
	private int d21;
	private int d22;
	private int d23;
	private int d24;
	private int d25;
	private int d26;
	private int d27;
	private int d28;
	private int d29;
	private int d30;
	private int d31;

	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int isActive;

	@jakarta.persistence.ManyToOne
	@jakarta.persistence.JoinColumn(name = "sId", insertable = false, updatable = false)
	private Admission student;

}
