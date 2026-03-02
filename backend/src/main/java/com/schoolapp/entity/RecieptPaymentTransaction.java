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
public class RecieptPaymentTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int recieptPaymentTransactionId;
	private int customerId;
	private int preBalance;
	private Date trDate;
	private int trnTypeId;
	private Date chequeDate;
	private int chequeNo;
	private int trnAmount;
	private int paymentRcvId;
	private String description;
	private int userId;
	private int branchId;
	private int orgId;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
}
