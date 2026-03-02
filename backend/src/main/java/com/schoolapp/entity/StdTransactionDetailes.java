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
public class StdTransactionDetailes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int stdTransactionDetailesId;
	private int stdAnnualFeesId;
	private int preBalance;
	private Date trDate;
	private Date chequeDate;
	private int chequeNo;
	private int recivedAmt;
	private int paymentRcvId;
	private String description;
	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}
