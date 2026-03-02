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
public class LeadAccounts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leadId;
	private Date date;
	private String cName;
	private String ownerName;
	private int ownerContact;
	private String panNo;
	private String gstNo;
	private String email;
	private String website;
	private int phone;
	private int fax;
	private String invoiceAddress;
	private int resourceId;
	private int statusId;
	private int assignId;
	private int stateId;
	private int distId;
	private int cityId;
	private int userId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private int branchId;
	private int orgId;
	private int income;
	private String incomeSource;
	private int otherIncome;
	private String otherIncomeSource;
	private int budget;
	private int budget1;
	private int budget2;
	private String notes;
	private String area;
	private int isActive;

	public int getBudgetOne() {
		return budget1;
	}

	public void setBudgetOne(int budgetOne) {
		this.budget1 = budgetOne;
	}

	public int getBudgetTwo() {
		return budget2;
	}

	public void setBudgetTwo(int budgetTwo) {
		this.budget2 = budgetTwo;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}
}
