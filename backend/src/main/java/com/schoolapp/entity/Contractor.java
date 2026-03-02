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
public class Contractor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int contractorId;
	private Date date;
	private String contractorName;
	private String ownerName;
	private int ownerContact;
	private String panNo;
	private String gstNo;
	private String email;
	private String website;
	private int phone;
	private int fax;
	private String invoiceAddress;
	private int income;
	private String incomeSource;
	private int otherIncome;
	private String otherIncomeSource;
	private int budget1;
	private int budget2;
	private String notes;
	private int isActive;
	private int stateId;
	private int distId;
	private int cityId;
	private int userId;
	private int orgId;
	private int branchId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;

}
