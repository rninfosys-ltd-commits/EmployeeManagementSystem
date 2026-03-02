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
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int uId;
	private int userSrNo;
	private String userName;
	private int branchId;
	private int orgId;
	private Date date;
	private String userContact;
	private String panNo;
	private String gstNo;
	private String email;
	private String password;
	private String phone;
	private String uAddress;
	private int income;
	private String incomeSource;
	private int otherIncome;
	private String otherIncomeSource;
	private String notes;
	private int isActive;
	private int stateId;
	private int distId;
	private int cityId;
	private int userId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getuAddress() {
		return uAddress;
	}

	public void setuAddress(String uAddress) {
		this.uAddress = uAddress;
	}
}
